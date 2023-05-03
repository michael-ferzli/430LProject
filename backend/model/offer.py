from ..app import db, ma
import datetime

class Offer(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    usd_amount = db.Column(db.Float)
    lbp_amount = db.Column(db.Float)
    usd_to_lbp = db.Column(db.Boolean)
    added_date = db.Column(db.DateTime)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=True)
    accepted = db.Column(db.Boolean)
    accepted_user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=True)

    def __init__(self, usd_amount, lbp_amount, usd_to_lbp, user_id):
        super(Offer, self).__init__(usd_amount=usd_amount,lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp, user_id=user_id, added_date=datetime.datetime.now(), accepted=False, accepted_user_id=None)

class OfferSchema(ma.Schema):
    class Meta:
        fields = ("id", "usd_amount", "lbp_amount", "usd_to_lbp", "user_id", "added_date","accepted","accepted_user_id")
        model = Offer
offer_schema = OfferSchema()