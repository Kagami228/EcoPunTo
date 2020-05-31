const functions = require('firebase-functions');
const admin = require('firebase-admin');
var express = require('express');

var app = express();
const cors = require('cors')
admin.initializeApp();
//functions.config().firebase
app.use(express.json()); // for parsing application/json
app.use(cors({ origin: true }));

app.post('/uplink', async function (req, res, next) {
  let db = admin.firestore();
  console.log(req.body);
  //if (req.body.payload_fields.lat !== 0 && req.body.payload_fields.long !== 0) {
  await db.collection("messages").add(req.body);
  await db.collection("events").add({ ...req.body.payload_fields, id: req.body.dev_id, time: req.body.metadata.time });
  await db.collection("state").doc(req.body.dev_id).set({ ...req.body.payload_fields, id: req.body.dev_id, time: req.body.metadata.time });


  //}
  res.json({ result: true });
});



exports.api = functions.https.onRequest(app);


