'use strict';

var sort = require('./sort.js');
var path = require('path');

var angularFilesort = function(emitter, logger, basePath, config) {
	config = typeof config === 'object' ? config : {};
	var log = logger.create('karma-angular-filesort');

	// Normalize whitelist paths against basePath
	config.whitelist = (config.whitelist || []).map(function (subPath) {
		return path.resolve(basePath, subPath);
	});

	// The file list is sorted by intercepting the file_list_modified event as Vojta Jina describes here:
	// https://github.com/karma-runner/karma/issues/851#issuecomment-30290071
	var originalEmit = emitter.emit;
	emitter.emit = function (event, files) {
		if (event === 'file_list_modified') {
			// Only included files are sorted, as they're the ones loaded into the document
			files.included = sort(files.included, log, config);
			originalEmit.call(emitter, event, files);
		} else {
			originalEmit.apply(emitter, arguments);
		}
	};
};

angularFilesort.$inject = ['emitter', 'logger', 'config.basePath', 'config.angularFilesort'];

module.exports = {
	'framework:angular-filesort': ['factory', angularFilesort]
};
