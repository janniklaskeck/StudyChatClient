/*
 * GET home page.
 */
var http = require('http');
exports.index = function(req, resa) {

	var options = {
		host : 'dachatserver.mybluemix.net',
		port : 80,
		path : '/message',
		method : 'GET'
	};

	http.request(options, function(res) {
		res.setEncoding('utf8');
		res.on('data', function(chunk) {
			resa.render('index', {
				title : 'Express',
				msgs : chunk
			});
		});
	}).end();

	
};