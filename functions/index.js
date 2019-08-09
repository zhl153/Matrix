//const functions = require('firebase-functions');
//
//// // Create and Deploy Your First Cloud Functions
//// // https://firebase.google.com/docs/functions/write-firebase-functions
////
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

var functions = require('firebase-functions');
var admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

//Listens change from firebase database
exports.sendNotification = functions.database.ref('/events/{pushId}')
     .onWrite((change, context) => {
         if (change.before.exists()) {
              return null;
         }
         // Grab the current value of what was written to the Realtime Database.
         var eventSnapshot = change.after.val();

         var topic = "android";

         var payload = {
             data: {
                 id : eventSnapshot.id,
                 type : eventSnapshot.event_type,
                 description : eventSnapshot.event_description
             }
         };

     // Send firebase cloud message to devices subscribed to the provided topic.
     return admin.messaging().sendToTopic(topic, payload)
         .then(function (response) {
             // See the MessagingTopicResponse reference documentation for the
             // contents of response.
             console.log("Successfully sent message:", response);
             return -1;
         })
         .catch(function (error) {
             console.log("Error sending message:", error);
         });
     })

