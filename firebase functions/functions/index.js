const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.notifications = functions.database
        .ref('notifications/{key}')
        .onCreate((snap, context) => {
            const key = context.params.key
            console.log('Key : ' + key)
            const tokenString = String(snap.child('token').val())
            console.log('Token : ' + tokenString)


            const message = {
                data : {
                    title : String(snap.child('titulo').val()),
                    body : String(snap.child('message').val()),
                    remetente : String(snap.child('remetente').val())
                }
            }

            admin.messaging().sendToDevice(tokenString, message)
            .then((response) => {
                console.log("Successfully send message:", response);
            })
            .catch((error) => {
                console.log("Error sending message:", error)
            });

            admin.database().ref('notifications/' + key ).remove()

            return Promise.resolve(0)
        })