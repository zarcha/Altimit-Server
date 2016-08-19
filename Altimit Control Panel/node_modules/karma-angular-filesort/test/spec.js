'use strict';

var chai = require('chai');
var expect = chai.expect;
var sinon = require('sinon');
chai.use(require('sinon-chai'));
var fs = require('fs');
var path = require('path');
var sortFiles = require('../lib/index.js')['framework:angular-filesort'][1];
var Q = require('q');

describe('karma-angular-filesort', function () {
	var emit;
	var emitter;
	var logger;

	beforeEach(function () {
		emit = sinon.spy();
		emitter = {
			emit: emit
		};
		var noop = function () {};
		logger = {
			create: function () {
				return {error: noop, warn: noop, info: noop, debug: noop};
			}
		};
	});

	function loadFixture(file) {
		var filePath = path.join(__dirname, 'fixture', file);
		return {
			path: filePath,
			content: fs.readFileSync(filePath).toString()
		};
	}

	function emitFiles(files) {
		emitter.emit('file_list_modified', files.reduce(function (batch, file) {
			var fixture = loadFixture(file);
			batch.served.push(fixture);
			batch.included.push(fixture);
			return batch;
		}, {served: [], included: []}));
	}

	function verifyPromise(promise, verify, done) {
		Q.resolve(promise)
			.then(verify)
			.then(done)
			.catch(function (error) {
				done(error);
			});
	}

	it('should call the original emit method with emitter scope', function () {
		sortFiles(emitter, logger, __dirname);
		emitFiles(['unrelated1.js']);
		expect(emit).to.have.been.calledOn(emitter);
	});

	it('should reorder files with module definition dependencies', function (done) {
		sortFiles(emitter, logger, __dirname);
		emitFiles(['dependsOnA.js', 'definesA.js']);
		expect(emit).to.have.been.calledWith('file_list_modified', sinon.match.any);
		verifyPromise(emit.args[0][1], function (files) {
			expect(files.included[0].path).to.match(/definesA\.js$/);
			expect(files.included[1].path).to.match(/dependsOnA\.js$/);
		}, done);
	});

	it('should preserve the order of unaffected files', function (done) {
		sortFiles(emitter, logger, __dirname);
		emitFiles(['unrelated1.js', 'unrelated2.js', 'dependsOnA.js', 'definesA.js', 'unrelated3.js', 'unrelated4.js']);
		expect(emit).to.have.been.calledWith('file_list_modified', sinon.match.any);
		verifyPromise(emit.args[0][1], function (files) {
			expect(files.included[0].path).to.match(/unrelated1\.js$/);
			expect(files.included[1].path).to.match(/unrelated2\.js$/);
			expect(files.included[2].path).to.match(/definesA\.js$/);
			expect(files.included[3].path).to.match(/dependsOnA\.js$/);
			expect(files.included[4].path).to.match(/unrelated3\.js$/);
			expect(files.included[5].path).to.match(/unrelated4\.js$/);
		}, done);
	});

	it('should put sorted files at the beginning of the subset they define', function (done) {
		sortFiles(emitter, logger, __dirname);
		emitFiles(['unrelated1.js', 'dependsOnA.js', 'unrelated2.js', 'definesA.js', 'unrelated3.js']);
		expect(emit).to.have.been.calledWith('file_list_modified', sinon.match.any);
		verifyPromise(emit.args[0][1], function (files) {
			expect(files.included[0].path).to.match(/unrelated1\.js/);
			expect(files.included[1].path).to.match(/definesA\.js/);
			expect(files.included[2].path).to.match(/dependsOnA\.js/);
			expect(files.included[3].path).to.match(/unrelated2\.js/);
			expect(files.included[4].path).to.match(/unrelated3\.js/);
		}, done);
	});

	it('should not reorder ng and ngLocale (Angular own) modules', function (done) {
		sortFiles(emitter, logger, __dirname);
		emitFiles(['unrelated1.js', 'unrelated2.js', 'dependsOnA.js', 'definesA.js', 'definesNg.js', 'unrelated3.js', 'unrelated4.js', 'definesNgLocale.js']);
		expect(emit).to.have.been.calledWith('file_list_modified', sinon.match.any);
		verifyPromise(emit.args[0][1], function (files) {
			expect(files.included[0].path).to.match(/unrelated1\.js$/);
			expect(files.included[1].path).to.match(/unrelated2\.js$/);
			expect(files.included[2].path).to.match(/definesA\.js$/);
			expect(files.included[3].path).to.match(/dependsOnA\.js$/);
			expect(files.included[4].path).to.match(/definesNg\.js$/);
			expect(files.included[5].path).to.match(/unrelated3\.js$/);
			expect(files.included[6].path).to.match(/unrelated4\.js$/);
			expect(files.included[7].path).to.match(/definesNgLocale\.js$/);
		}, done);
	});

	it('should allow a whitelist to restrict which files are reordered', function (done) {
		sortFiles(emitter, logger, __dirname, {whitelist: ['fixture/definesA.js', 'fixture/dependsOnA.js']});
		emitFiles(['definesB.js', 'unrelated1.js', 'dependsOnA.js', 'definesA.js']);
		expect(emit).to.have.been.calledWith('file_list_modified', sinon.match.any);
		verifyPromise(emit.args[0][1], function (files) {
			expect(files.included[0].path).to.match(/definesB\.js$/);
			expect(files.included[1].path).to.match(/unrelated1\.js$/);
			expect(files.included[2].path).to.match(/definesA\.js$/);
			expect(files.included[3].path).to.match(/dependsOnA\.js$/);
		}, done);
	});

	it('should support pattern matching in the whitelist', function (done) {
		sortFiles(emitter, logger, __dirname, {whitelist: ['fixture/*A.js']});
		emitFiles(['definesB.js', 'unrelated1.js', 'dependsOnA.js', 'definesA.js']);
		expect(emit).to.have.been.calledWith('file_list_modified', sinon.match.any);
		verifyPromise(emit.args[0][1], function (files) {
			expect(files.included[0].path).to.match(/definesB\.js$/);
			expect(files.included[1].path).to.match(/unrelated1\.js$/);
			expect(files.included[2].path).to.match(/definesA\.js$/);
			expect(files.included[3].path).to.match(/dependsOnA\.js$/);
		}, done);
	});

	it('should not reorder files if no angular modules are found', function (done) {
		sortFiles(emitter, logger, __dirname);
		emitFiles(['unrelated2.js', 'unrelated1.js']);
		expect(emit).to.have.been.calledWith('file_list_modified', sinon.match.any);
		verifyPromise(emit.args[0][1], function (files) {
			expect(files.included[0].path).to.match(/unrelated2\.js$/);
			expect(files.included[1].path).to.match(/unrelated1\.js$/);
		}, done);
	});

	it('should pass through other events', function () {
		sortFiles(emitter, logger, __dirname);
		emitter.emit('some_other_event', 'arg1', 'arg2', 'arg3');
		expect(emit).to.have.been.calledWith('some_other_event', 'arg1', 'arg2', 'arg3');
	});
});
