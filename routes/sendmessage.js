/**
 * http://usejsdoc.org/
 */

var http = require('http');

exports.send = function(req, resa) {
	var options = {
		host : 'dachatserver.mybluemix.net',
		port : 80,
		path : '/message',
		method : 'POST'
	};

	var request = http.request(options, function(res) {
		resa.redirect("/");
	});
	var user = req.query.Username;
	var msg = req.query.Message;
	req.session.user = user;
	request.write("{'username':'" + user + "','content':'" + msg + "'}");
	request.end();

};