/*
 * GET home page.
 */

var backgroundCheckRunning = false;

function parseMessages(jsonString) {
	var parsed = JSON.parse(jsonString);
	var result = "";
	if (parsed.hasOwnProperty("messages")) {
		for (var i = 0; i < parsed.messages.length; i++) {
			var currentMessage = parsed.messages[i];
			var dateParsed = Date.parse(currentMessage.datePosted);
			var dateString = new Date(dateParsed).toLocaleString();
			result += dateString;
			result += " ";
			result += currentMessage.user;
			result += ": ";
			result += currentMessage.content;
			result += "\n";
		}
	}
	return result;
}
var messageList = "";

var http = require('http');
function getMessagesBG() {
	var options = {
		host : 'dachatserver.mybluemix.net',
		port : 80,
		path : '/message',
		method : 'GET'
	};

	http.request(options, function(res) {
		res.setEncoding('utf8');
		res.on('data', function(chunk) {
			messageList = parseMessages(chunk);
			console.log(messageList);
		});
	}).end();
}

exports.index = function(req, resa) {
	if (!backgroundCheckRunning) {
		setInterval(function() {
			getMessagesBG();
		}, 5000);

		backgroundCheckRunning = true;
	}
	var userName = req.session.user;
	if (userName === '') {
		userName = "anon";
	}
	resa.render('index', {
		title : 'DaChatApp',
		msgs : messageList,
		user : userName
	});
};
