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
	
	const request = http.request(options, function(res) {
		resa.redirect("/");
	});
	var user = req.query.user;
	var msg = req.query.message;
	request.write("{'username':'" + user + "','content':'" + msg + "'}");
	request.end();
	
}