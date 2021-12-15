const http = require('http');
const querystring = require('querystring');

function loadDir(path) {
    const parameters = { path: path }

    const get_request_args = querystring.stringify(parameters);

    var options = {
        host: 'localhost',
        port: 8080,
        path: '/directory?' + get_request_args,
        method: 'PUT'
    };

    var req = http.request(options, function (res) {
        console.log('STATUS: ' + res.statusCode);
        console.log('HEADERS: ' + JSON.stringify(res.headers));
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            console.log('BODY: ' + chunk);
        });
    });

    req.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });

    req.end();
}

function listDirs() {
    var options = {
        host: 'localhost',
        port: 8080,
        path: '/directories',
        method: 'GET'
    };

    var req = http.request(options, function (res) {
        console.log('STATUS: ' + res.statusCode);
        console.log('HEADERS: ' + JSON.stringify(res.headers));
        res.setEncoding('utf8');
        res.on('data', function (chunk) {
            console.log('BODY: ' + chunk);
        });
    });

    req.on('error', function (e) {
        console.log('problem with request: ' + e.message);
    });

    req.end();
}

listDirs()