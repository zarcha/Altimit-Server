'use strict';

var path = require('path');
var gulp = require('gulp');
var conf = require('./conf');
var browserSync = require('browser-sync')

var $ = require('gulp-load-plugins')({
  pattern: ['gulp-*', 'main-bower-files', 'uglify-save-license', 'del']
});

var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var minifycss = require('gulp-clean-css');
var runSequence = require('run-sequence');
var watch = require('gulp-watch');

var mainDest = "dist/";

function isOnlyChange(event) {
  return event.type === 'changed';
}

//Moveing the html files
gulp.task('moveViews', function(){
    //All views
    gulp.src(['src/views/*.html'])
        .pipe(gulp.dest(mainDest + 'views'));

    //Index needs to be moved differently
    gulp.src(['src/index.html'])
        .pipe(gulp.dest(mainDest));
});

//Css file compressions
gulp.task('minCss', function(){
    //Custom css
    gulp.src(['src/styles/*.css'])
        .pipe(minifycss())
        .pipe(concat('app.min.css'))
        .pipe(gulp.dest(mainDest + 'css'));

    //Bower css
    gulp.src(['node_modules/bootstrap/dist/css/bootstrap.min.css'])
        .pipe(minifycss())
        .pipe(concat('vendor.min.css'))
        .pipe(gulp.dest(mainDest + 'css'));
});

gulp.task('moveImages', function () {
  gulp.src(['src/images/*.*'])
    .pipe(gulp.dest(mainDest + '/images'));
});

//JavsScript file compressions
gulp.task('minJs', function () {
    //Bower files need to be in one file
    gulp.src(['bower_components/angular/angular.min.js',
        'bower_components/jquery/dist/jquery.min.js',
        'bower_components/angular-route/angular-route.min.js',
        'node_modules/bootstrap/dist/js/bootstrap.min.js'])
        .pipe(concat('deps.js'))
        //.pipe(gulp.dest('src'))
        .pipe(rename('deps.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(mainDest + 'scripts'));

    gulp.src(['src/app.route.js', 'src/components/**/*.*.js'])
        .pipe(concat('main.js'))
        //.pipe(gulp.dest('src'))
        .pipe(rename('app.min.js'))
        //.pipe(uglify())
        .pipe(gulp.dest(mainDest + 'scripts'));
});

gulp.task('moveFonts', function(){
    gulp.src(['node_modules/bootstrap/dist/fonts/*.*'])
        .pipe(gulp.dest(mainDest + 'fonts'));
});

gulp.task('altimitWatch', function(){
  gulp.watch('src/app.js', ['minJs'], function () {
    if(isOnlyChange(event)){
      browserSync.reload(event.path, {stream: true});
    }
  });
  gulp.watch('src/styles/*.css', ['minCss'], function (event) {
    if(isOnlyChange(event)){
      browserSync.reload(event.path, {stream: true});
    }
  });
  gulp.watch('src/index.html', ['moveViews'], function (event) {
    if(isOnlyChange(event)){
      browserSync.reload();
    }
  });
  gulp.watch('src/views/*.html', ['moveViews'], function (event) {
    if(isOnlyChange(event)){
      browserSync.reload();
    }
  });
});

gulp.task('clean', function () {
  return $.del([path.join(conf.paths.dist, '/'), path.join(conf.paths.tmp, '/')]);
});

gulp.task('altimit_build', ['minJs', 'moveFonts', 'minCss', 'moveViews', 'moveImages', 'altimitWatch']);
