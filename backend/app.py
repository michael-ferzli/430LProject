import datetime
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask import request
from flask import jsonify
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt
from flask import abort
import jwt
from .db_config import DB_CONFIG
import numpy as np

app = Flask(__name__)
ma = Marshmallow(app)
bcrypt = Bcrypt(app)

SECRET_KEY = "b'|\xe7\xbfU3`\xc4\xec\xa7\xa9zf:}\xb5\xc7\xb9\x139^3@Dv'"

app.config['SQLALCHEMY_DATABASE_URI'] = DB_CONFIG
CORS(app)
db = SQLAlchemy(app)

from .model.user import User, user_schema
from .model.transaction import Transaction, TransactionSchema, transaction_schema
from .model.message import Message, MessageSchema, message_schema
from .model.offer import Offer, OfferSchema, offer_schema


def create_token(user_id):
    payload = {
        'exp': datetime.datetime.utcnow() + datetime.timedelta(days=4),
        'iat': datetime.datetime.utcnow(),
        'sub': user_id
    }
    return jwt.encode(
        payload,
        SECRET_KEY,
        algorithm='HS256'
)

@app.route('/graph', methods = ['GET'])
def get_all_transactions():
    transactions = Transaction.query.all()
    return jsonify(transaction_schema.dump(transactions, many=True))

@app.route('/acceptOffer', methods = ['POST'])
def accept_offer():
    offer_id = request.json['offer_id']
    offer = Offer.query.filter_by(id=offer_id).first()
    offer.accepted = True
    offer.accepted_user_id = decode_token(extract_auth_token(request))
    if offer.accepted_user_id == offer.user_id:
        abort(403)
    db.session.commit()
    return jsonify(offer_schema.dump(offer))

@app.route('/getOffers', methods = ['GET'])
def get_offers():
    offers = Offer.query.all()
    return jsonify(offer_schema.dump(offers, many=True))

@app.route('/postOffer', methods = ['POST'])
def post_offer():
    usd_amount = request.json['usd_amount']
    lbp_amount = request.json['lbp_amount']
    usd_to_lbp = request.json['usd_to_lbp']
    if extract_auth_token(request):
        try:
            user_id = decode_token(extract_auth_token(request))
        except:
            abort(403)
    else:
        abort(403)
    new_offer = Offer(usd_amount, lbp_amount, usd_to_lbp, user_id)
    db.session.add(new_offer)
    db.session.commit()
    return jsonify(offer_schema.dump(new_offer))

@app.route('/postMessage', methods = ['POST'])
def post_message():
    message = request.json['message']
    if extract_auth_token(request):
        try:
            user_id = decode_token(extract_auth_token(request))
        except:
            abort(403)
    else:
        user_id=None 
    new_message = Message(user_id, message)
    db.session.add(new_message)
    db.session.commit()
    return jsonify(message_schema.dump(new_message))

@app.route('/getMessages', methods = ['GET'])
def get_messages():
    messages = Message.query.all()
    return jsonify(message_schema.dump(messages, many=True))

@app.route('/getUserFromId', methods = ['GET'])
def get_user_from_id():
    user_id = request.json['user_id']
    user = User.query.filter_by(id=user_id).first()
    return jsonify(user_schema.dump(user))

@app.route('/statistics', methods = ['GET'])
def get_statistics():
    time_now = datetime.datetime.now()
    one_day = datetime.datetime.now() - datetime.timedelta(days=1)
    one_week = datetime.datetime.now() - datetime.timedelta(days=7)
    one_month = datetime.datetime.now() - datetime.timedelta(days=30)
    one_year = datetime.datetime.now() - datetime.timedelta(days=365)
    U2L_day = Transaction.query.filter(Transaction.added_date.between(one_day, time_now),Transaction.usd_to_lbp == True).all()
    L2U_day = Transaction.query.filter(Transaction.added_date.between(one_day, time_now),Transaction.usd_to_lbp == False).all()
    U2L_week = Transaction.query.filter(Transaction.added_date.between(one_week, time_now),Transaction.usd_to_lbp == True).all()
    L2U_week = Transaction.query.filter(Transaction.added_date.between(one_week, time_now),Transaction.usd_to_lbp == False).all()
    U2L_month = Transaction.query.filter(Transaction.added_date.between(one_month, time_now),Transaction.usd_to_lbp == True).all()
    L2U_month = Transaction.query.filter(Transaction.added_date.between(one_month, time_now),Transaction.usd_to_lbp == False).all()
    U2L_year = Transaction.query.filter(Transaction.added_date.between(one_year, time_now),Transaction.usd_to_lbp == True).all()
    L2U_year = Transaction.query.filter(Transaction.added_date.between(one_year,time_now),Transaction.usd_to_lbp == False).all()

    all_U2L_list = [U2L_day, U2L_week, U2L_month, U2L_year]
    all_L2U_list = [L2U_day, L2U_week, L2U_month, L2U_year]
    
    print(U2L_year)
    U2L_values = []
    U2L_std = []
    U2L_max = []
    U2L_min = []
    for i in all_U2L_list:
        if len(i)!=0:
            min = i[0].lbp_amount/i[0].usd_amount
            max = i[0].lbp_amount/i[0].usd_amount
            U2L = 0
            for j in i:
                print("HI")
                rate = j.lbp_amount/j.usd_amount
                U2L+=rate
                if rate > max:
                    max = rate
                if rate < min:
                    min = rate

                if len(i)!=0:
                    U2L = round(U2L/len(i),2)
                else:
                    U2L = -1

            U2L_values.append(U2L)
            U2L_std.append(np.std(U2L))
            U2L_min.append(min)
            U2L_max.append(max)
        else:
            U2L_values.append(0)
            U2L_std.append(0)
            U2L_max.append(0)
            U2L_min.append(0)
        

    L2U_values = []
    L2U_std = []
    L2U_max = []
    L2U_min = []
    for i in all_L2U_list:
        if len(i)!=0:
            L2U = 0
            min = i[0].lbp_amount/i[0].usd_amount
            max = i[0].lbp_amount/i[0].usd_amount
            for j in i:
                rate = j.lbp_amount/j.usd_amount
                L2U+=rate
                if rate > max:
                    max = rate
                if rate < min:
                    min = rate
                if len(i)!=0:
                    L2U = round(L2U/len(i),2)
                else:
                    L2U = -1
        
            L2U_values.append(L2U)
            L2U_std.append(np.std(L2U))
            L2U_max.append(max)
            L2U_min.append(min)
        else:
            L2U_values.append(0)
            L2U_std.append(0)
            L2U_max.append(0)
            L2U_min.append(0)


    data = {
        "usd_to_lbp": {
            "day": U2L_values[0],
            "week": U2L_values[1],
            "month": U2L_values[2],
            "year": U2L_values[3],
        },
        "lbp_to_usd": {
            "day": L2U_values[0],
            "week": L2U_values[1],
            "month": L2U_values[2],
            "year": L2U_values[3],
        },
        "usd_to_lbp_std": {
            "day": U2L_std[0],
            "week": U2L_std[1],
            "month": U2L_std[2],
            "year": U2L_std[3],
        },
        "lbp_to_usd_std": {
            "day": L2U_std[0],
            "week": L2U_std[1],
            "month": L2U_std[2],
            "year": L2U_std[3],
        },
        "usd_to_lbp_max": {
            "day": U2L_max[0],
            "week": U2L_max[1],
            "month": U2L_max[2],
            "year": U2L_max[3],
        },
        "lbp_to_usd_max": {
            "day": L2U_max[0],
            "week": L2U_max[1],
            "month": L2U_max[2],
            "year": L2U_max[3],
        },
        "usd_to_lbp_min": {
            "day": U2L_min[0],
            "week": U2L_min[1],
            "month": U2L_min[2],
            "year": U2L_min[3],
        },
        "lbp_to_usd_min": {
            "day": L2U_min[0],
            "week": L2U_min[1],
            "month": L2U_min[2],
            "year": L2U_min[3],
        },


    }
 
    resp = jsonify(data)
    return resp
    

@app.route('/authenticate', methods=['POST'])
def authenticate_user():
    if "user_name" not in request.json or "password" not in request.json:
        abort(400)
    user = request.json["user_name"]
    pas = request.json["password"]
    queryUser = User.query.filter_by(user_name=user).first()
    if queryUser == None:
        abort(403)
    if not bcrypt.check_password_hash(queryUser.hashed_password,pas):
        abort(403)
    return jsonify(token=create_token(queryUser.id))



@app.route('/user', methods=['POST'])
def post_user():
    u = User(request.json["user_name"],request.json["password"])
    db.session.add(u)
    db.session.commit()
    return jsonify(user_schema.dump(u))

@app.route('/transaction', methods=['POST'])
def post_transaction():
    usd_amount=request.json["usd_amount"]
    lbp_amount=request.json["lbp_amount"]
    usd_to_lbp=request.json["usd_to_lbp"]
    if extract_auth_token(request):
        try:
            user_id = decode_token(extract_auth_token(request))
        except:
            abort(403)
    else:
        user_id=None 
    t = Transaction(usd_amount,lbp_amount,usd_to_lbp,user_id)
    db.session.add(t)
    db.session.commit()
    return jsonify(transaction_schema.dump(t))

@app.route('/transaction', methods=['GET'])
def get_transaction():
    if extract_auth_token(request):
        try:
            user_id = decode_token(extract_auth_token(request))
        except:
            abort(403)
        transactions = Transaction.query.filter_by(user_id=user_id).all()
    else:
        transactions = Transaction.query.all()
    transactions_schema = TransactionSchema(many=True)
    return jsonify(transactions_schema.dump(transactions))

@app.route('/exchangeRate', methods=['GET'])
def get_exchange_rate():
    U2L = 0
    L2U = 0 
    START_DATE = datetime.datetime.now() - datetime.timedelta(days=3)
    END_DATE = datetime.datetime.now()
    U2Lentries = Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),Transaction.usd_to_lbp == True).all()
    L2Uentries = Transaction.query.filter(Transaction.added_date.between(START_DATE, END_DATE),Transaction.usd_to_lbp == False).all()
    for i in U2Lentries:
        U2L+=i.lbp_amount/i.usd_amount
    for i in L2Uentries:
        L2U+=i.lbp_amount/i.usd_amount
    if len(U2Lentries)!=0:
        U2L = round(U2L/len(U2Lentries),2)
    if len(L2Uentries)!=0:
        L2U = round(L2U/len(L2Uentries),2)
    if U2L == 0 :
        U2L="NO ENTRIES YET"
    if L2U == 0 :
        L2U="NO ENTRIES YET"
    resp = jsonify(usd_to_lbp=U2L,lbp_to_usd=L2U)
    return resp

def extract_auth_token(authenticated_request):
    auth_header = authenticated_request.headers.get('Authorization')
    if auth_header:
        return auth_header.split(" ")[1]
    else:
        return None
def decode_token(token):
    payload = jwt.decode(token, SECRET_KEY, 'HS256')
    return payload['sub']
