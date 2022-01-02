function sendNotification(data) {
  var headers = {
    "Content-Type": "application/json; charset=utf-8",
    Authorization: "Basic NDU3ZmY1NDQtZjRhNy00ZDVlLTllYWEtMTg2NTllNTY3N2E1",
  };

  var options = {
    host: "onesignal.com",
    port: 443,
    path: "/api/v1/notifications",
    method: "POST",
    headers: headers,
  };

  var https = require("https");
  var req = https.request(options, function (res) {
    res.on("data", function (data) {
      console.log("Response:");
      console.log(JSON.parse(data));
    });
  });

  req.on("error", function (e) {
    console.log("ERROR:");
    console.log(e);
  });
  req.write(JSON.stringify(data));
  req.end();
}

const message = {
  app_id: "62e7212c-d7df-4c9c-8451-246b84c3e3d3",
  contents: { en: "e" },
  included_segments: ["All"],
  headings: { en: "This Is A Title internet test" },
  priority: 10,
};
exports.sendNotification = sendNotification;
exports.testmessage = message;
