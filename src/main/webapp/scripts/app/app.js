'use strict';

angular.module('burnsearchApp', ['LocalStorageModule',
               'ui.bootstrap', // for modal dialogs
    'ngResource', 'ui.router', 'ngCookies', 'ngCacheBuster', 'infinite-scroll', 'angularMoment',
    'cgBusy'])

    .run(function ($rootScope, $location, $window, $http, $state,  Auth, Principal, ENV, VERSION) {
        moment.tz.add('America/Los_Angeles|PST PDT|80 70|0101|1Lzm0 1zb0 Op0');
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            var titleKey = 'burnsearch' ;

            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }
            $window.document.title = titleKey;
        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
        function selectBackground() {
            var images = ['playa-ground.jpg', 'embrace-burn.jpg', 'buddha-on-wheels.jpg',
                'wide-open-playa.jpg', 'leaving-the-gates.jpg', 'the-playa-sunset.jpg',
                'aerial-view-whiteout.jpg', 'library-of-babel.jpg', 'night-temple.jpg',
                'pumping-up-down.jpg'];
            var titles = ['playa', 'Embrace Burn', 'Buddha on Wheels', 'The WIDE OPEN Playa',
                'Leaving the gates of the Temple of Grace', 'The Playa',
                'Aerial View of a Whiteout', 'Library of Babel by Warrick Macmillan',
                'Night Temple', 'Pumping up and Down'];
            var photographers = ['Mario Covic', 'Anne Staveley', 'Dust to Ashes',
                'Michael DiMonaco', 'Michael DiMonaco', 'Mark Hammon', 'Burning Man Staff',
                'Jonathan LaLiberty', 'John K. Goodman', 'Michael Muenzing'];
            var contacts = ['mailto:mariocovicphotos@gmail.com', 'mailto:annestaveley@gmail.com',
                'http://www.dusttoashes.com/', 'mailto:madimagingsf@gmail.com',
                'mailto:madimagingsf@gmail.com', 'http://www.hammonphoto.com/',
                'http://galleries.burningman.org/pages/BurningMan.com',
                'mailto:JonathanLaLiberty@gmail.com', 'mailto:john@johnkgoodman.com',
                'mailto:michael.muenzing@gmx.com'];
            var years = ['2010', '2014', '2014', '2014', '2014', '2014', '2003', '2014', '2014',
                '2014'];
            var imageChoice = Math.floor(Math.random() * images.length);
            $rootScope.backgroundImage = images[imageChoice];
            $rootScope.backgroundTitle = titles[imageChoice];
            $rootScope.backgroundPhotographer = photographers[imageChoice];
            $rootScope.backgroundContact = contacts[imageChoice];
            $rootScope.backgroundYear = years[imageChoice];
        }
        function setBackground(mediaQuery) {
            if (mediaQuery.matches) {
                angular.element('body').css({
                    'background-image': 'url("../assets/images/' + $rootScope.backgroundImage + '")'});
            } else {
                angular.element('body').css({'background-image': ''});
            }
            return mediaQuery;
        }
        selectBackground();
        setBackground($window.matchMedia('all and (min-width: 768px)')).addListener(setBackground);
    })
    .factory('authInterceptor', function ($rootScope, $q, $location, localStorageService) {
        return {
            // Add authorization token to headers
            request: function (config) {
                config.headers = config.headers || {};
                var token = localStorageService.get('token');

                if (token && token.expires_at && token.expires_at > new Date().getTime()) {
                    config.headers.Authorization = 'Bearer ' + token.access_token;
                }

                return config;
            }
        };
    })
    .factory('authExpiredInterceptor', function ($rootScope, $q, $injector, localStorageService) {
        return {
            responseError: function (response) {
                // token has expired
                if (response.status === 401 && (response.data.error == 'invalid_token' || response.data.error == 'Unauthorized')) {
                    localStorageService.remove('token');
                    var Principal = $injector.get('Principal');
                    if (Principal.isAuthenticated()) {
                        var Auth = $injector.get('Auth');
                        Auth.authorize(true);
                    }
                }
                return $q.reject(response);
            }
        };
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider,  httpRequestInterceptorCacheBusterProvider) {

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/components/navbar/navbar.html',
                    controller: 'NavbarController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ]
            }
        });

        $httpProvider.interceptors.push('authExpiredInterceptor');

        $httpProvider.interceptors.push('authInterceptor');

    })
    .value('cgBusyDefaults', {
        message: '',
        backdrop: false,
        templateUrl: 'scripts/components/progress/progress.template.html'
    });
