'use strict';

var gulp = require('gulp');
var gulpNgConfig = require('gulp-ng-config');

var $ = require('gulp-load-plugins')();

module.exports = function (options) {
  gulp.task('config', function () {
    gulp.src(options.src + '/app/config.json')
      .pipe(gulpNgConfig('Metanome.config', {
        environment: 'local'
      }))
      .pipe(gulp.dest(options.src + '/app/'))
  });

  gulp.task('config:build', function () {
    gulp.src(options.src + '/app/config.json')
      .pipe(gulpNgConfig('Metanome.config', {
        environment: 'production'
      }))
      .pipe(gulp.dest(options.src + '/app/'))
  });
};
