from ..app import db, ma
import datetime

class Message(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer)
    message = db.Column(db.String(150))
    added_date = db.Column(db.DateTime)

    def __init__(self, user_id, message):
        super(Message, self).__init__(user_id=user_id,message=message, added_date=datetime.datetime.now())

class MessageSchema(ma.Schema):
    class Meta:
        fields = ("id", "user_id", "message", "added_date")
        model = Message
message_schema = MessageSchema()