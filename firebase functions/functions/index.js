const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.notifications = functions.database
        .ref('notifications/{token}')
        .onCreate((snap, context) => {
            //const key = context.params.token
            const key = snap.key
            console.log('Key : ' + key)
            const token = snap.child('token').val()
            console.log('Token : ' + token)


            var message = {
                data : {
                    title : snap.child('titulo').val().toString(),
                    body : snap.child('message').val().toString(),
                    sender : snap.child('remetente').val().toString()
                },
                token : token.toString()
            }

            admin.messaging().send(message)
                .then((response) => {
                    console.log("Successfully send message", response);
                })
                .catch((error) => {
                    console.log("Error sending message", error)
                })

            admin.database().ref('notifications').remove(key)

            return Promise.resolve(0)
        })