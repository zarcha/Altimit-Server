'use strict';

// This file is a modified version of angular-filesort, adapted to operate on Karma's file object.
// Original version is here:
// https://github.com/klei/gulp-angular-filesort/blob/b3ae4a01bf72a3b2434239dc1c84cab156c8e73f/index.js

var ngDep = require('ng-dependencies');
var toposort = require('toposort');
var os = require('os');
var minimatch = require('minimatch');

var ANGULAR_MODULE = 'ng';

module.exports = function (files, log, config) {
	var moduleFiles = {};
	var sortNodes = [];
	var sortEdges = [];
	var subsetStart = null;

	// Examine the contents of each file and categorize it as either to-be-sorted or not-to-be-sorted:
	files = files.filter(function (file, index) {
		var willBeSorted = false;
		var deps;

		if (config.whitelist.length && !inWhitelist(file.path, config.whitelist)) {
			return true;
		}

		try {
			deps = ngDep(file.content);
		} catch (err) {
			log.debug('Error in parsing: "' + file.path + '", ' + err.message);
			return true;
		}

		if (deps.modules) {
			// Store references to each file with a declaration:
			Object.keys(deps.modules).forEach(function (name) {
				moduleFiles[name] = file;
				if (name !== ANGULAR_MODULE && name !== 'ngLocale') {
					willBeSorted = true;
				}
			});
		}

		if (deps.dependencies) {
			// Add each file with dependencies to the array to sort:
			deps.dependencies.forEach(function (dep) {
				if (isDependecyUsedInAnyDeclaration(dep, deps)) {
					return;
				}
				if (dep === ANGULAR_MODULE) {
					return;
				}
				sortEdges.push([file, dep]);
				willBeSorted = true;
			});
		}

		if (willBeSorted) {
			// Store the position of the first file to be sorted, as that's where the sorted subset will be re-inserted:
			if (subsetStart === null) {
				subsetStart = index;
			}
			sortNodes.push(file);
		}
		return !willBeSorted;
	});

	if (sortNodes.length === 0) {
		// No angular module references found, so return original array:
		return files;
	}

	// Convert all module names to actual files with declarations:
	for (var i = 0; i < sortEdges.length; i++) {
		var moduleName = sortEdges[i][1];
		var declarationFile = moduleFiles[moduleName];
		if (declarationFile) {
			sortEdges[i][1] = declarationFile;
		} else {
			// Depending on module outside list (possibly a 3rd party one),
			// don't care when sorting:
			sortEdges.splice(i--, 1);
		}
	}

	// Sort `files` with `toSort` as dependency tree:
	Array.prototype.splice.apply(files, [subsetStart, 0].concat(toposort.array(sortNodes, sortEdges).reverse()));

	log.debug('Sorted files:' + os.EOL + files.map(function (file) {
		return '\t' + file.path;
	}).join(os.EOL));

	return files;
};

function inWhitelist(filePath, whitelist) {
	return whitelist.some(function (whitelistPath) {
		return minimatch(filePath, whitelistPath);
	});
}

function isDependecyUsedInAnyDeclaration (dependency, ngDeps) {
	if (!ngDeps.modules) {
		return false;
	}
	if (dependency in ngDeps.modules) {
		return true;
	}
	return Object.keys(ngDeps.modules).some(function (module) {
		return ngDeps.modules[module].indexOf(dependency) > -1;
	});
}
