karma-angular-filesort [![Build Status](https://travis-ci.org/wilsonjackson/karma-angular-filesort.svg?branch=master)](https://travis-ci.org/wilsonjackson/karma-angular-filesort)
======================

> Sorts your AngularJS files to avoid `[$injector:nomod]` errors.

This plugin owes its existence to [gulp-angular-filesort](https://github.com/klei/gulp-angular-filesort), on which it is heavily based.

Installation
------------

    npm install --save-dev karma-angular-filesort

Compatibility
-------------

For Karma version 0.13.x, use version ~1.0 of this plugin. For older Karma versions, use 0.1.

Configuration
-------------

A simple example configuration would look something like this:

```js
// karma.conf.js
module.exports = function(config) {
  config.set({
    frameworks: ['jasmine', 'angular-filesort'],
    
    files: [
      'bower_components/angular/angular.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'app/**/*.js',
      'test/**/*.js'
    ],

    angularFilesort: {
      whitelist: [
        'app/**/*.js'
      ]
    }
  });
};
```

### Whitelist?

`karma-angular-filesort` will sort the narrowest possible subset of your files by selecting only files that reference angular modules and sorting them in-place. This alone can't prevent all issues though, as certain other files you're obliged to tell Karma about may also define modules — such as Angular itself — and sorting such files can be problematic.

The `whitelist` config option allows you to further narrow the subset of files `karma-angular-filesort` will sort for you. Each path in the whitelist array will be resolved against Karma's `basePath`. Patterns are supported via [minimatch](https://github.com/isaacs/minimatch).

License
-------

MIT
