var gulp = require('gulp');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var minifycss = require('gulp-clean-css');
var runSequence = require('run-sequence');
var watch = require('gulp-watch');
var connect = require('gulp-connect');

var mainDest = "dist/";

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

//JavsScript file compressions
gulp.task('minJs', function () {
  //Bower files need to be in one file
  gulp.src(['bower_components/angular/angular.min.js',
    'bower_components/jquery/dist/jquery.min.js',
    'bower_components/angular-route/angular-route.min.js',
    'node_modules/bootstrap/dist/js/bootstrap.min.js'])
    .pipe(concat('deps.js'))
    .pipe(gulp.dest('src'))
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

gulp.task('watch', function(){
  gulp.watch('src/app.js', ['minJs']);
  gulp.watch('src/components/**/*.*.js', ['minJs']);
  gulp.watch('src/services/**/*.php', ['movePhp']);
  gulp.watch('src/styles/*.css', ['minCss']);
  gulp.watch('src/index.html', ['moveViews']);
  gulp.watch('src/views/*.html', ['moveViews']);
});

gulp.task('connect', function () {
  connect.server();
});

gulp.task('default', function(){
  runSequence('minJs');
  runSequence('moveViews');
  runSequence('minCss');
  runSequence('moveFonts');
  runSequence('watch');
});
