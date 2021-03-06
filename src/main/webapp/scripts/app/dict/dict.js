'use strict';

egdApp
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/dict/dict.html',
                        controller: 'DictController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('dict');
                        return $translate.refresh();
                    }]
                }
            }).state('dict', {
                parent: 'site',
                url: '/dict',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/dict/dict.html',
                        controller: 'DictController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('dict');
                        return $translate.refresh();
                    }]
                }
            }).state('dictDetail', {
                parent: 'site',
                url: '/dict/:phrase',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/dict/dict.html',
                        controller: 'DictController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('dict');
                        return $translate.refresh();
                    }]
                }
            }).state('abbr', {
                parent: 'site',
                url: '/abbr',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/dict/abbr.html'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('dict');
                        return $translate.refresh();
                    }]
                }
            }).state('tos', {
                parent: 'site',
                url: '/tos',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/tos/tos.html'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('tos');
                        return $translate.refresh();
                    }]
                }
            });
    });
