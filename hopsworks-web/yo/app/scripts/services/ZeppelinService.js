/*
 * This file is part of HopsWorks
 *
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved.
 *
 * HopsWorks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HopsWorks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with HopsWorks.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict';

angular.module('hopsWorksApp')
        .factory('ZeppelinService', ['$http', '$websocket', function ($http, $websocket) {
            var websocketCalls = {};
            var setUp = function () {            
              websocketCalls.ws = $websocket(getZeppelinWsBaseURL() + getCookie('projectID'));
              websocketCalls.ws.reconnectIfNotNormalClose = true;

              websocketCalls.sendNewEvent = function (data) {
                data.principal = 'anonymous';
                data.ticket = 'anonymous';
                data.roles = '';
                console.log('Send >> %o, %o, %o, %o, %o', data.op, data.principal, data.ticket, data.roles, data);
                websocketCalls.ws.send(JSON.stringify(data));
              };

              websocketCalls.isConnected = function () {
                return (websocketCalls.ws.socket.readyState === 1);
              };
            };
            var destroy = function () {
              if (websocketCalls.isConnected) {
               websocketCalls.ws.close();
              }
              websocketCalls = {};
            };
            setUp();
            return {
              wsSetup: function () {
                setUp();
              },
              wsDestroy: function () {
                destroy();
              },
              websocket: function () {
                if (angular.equals(websocketCalls, {}) || 
                   !websocketCalls.isConnected) {
                  setUp();
                }
                return websocketCalls;
              },
              moveNoteToTrash: function (noteId) {
                websocketCalls.sendNewEvent({op: 'MOVE_NOTE_TO_TRASH', data: {id: noteId}});
              },
              moveFolderToTrash: function (folderId) {
                websocketCalls.sendNewEvent({op: 'MOVE_FOLDER_TO_TRASH', data: {id: folderId}});
              },
              restoreNote: function (noteId) {
                websocketCalls.sendNewEvent({op: 'RESTORE_NOTE', data: {id: noteId}});
              },
              restoreFolder: function (folderId) {
                websocketCalls.sendNewEvent({op: 'RESTORE_FOLDER', data: {id: folderId}});
              },
              restoreAll: function () {
                websocketCalls.sendNewEvent({op: 'RESTORE_ALL'});
              },
              deleteNote: function (noteId) {
                websocketCalls.sendNewEvent({op: 'DEL_NOTE', data: {id: noteId}});
              },
              removeFolder: function (folderId) {
                websocketCalls.sendNewEvent({op: 'REMOVE_FOLDER', data: {id: folderId}});
              },
              emptyTrash: function () {
                websocketCalls.sendNewEvent({op: 'EMPTY_TRASH'});
              },
              settings: function () {
                return $http.get('/api/zeppelin/'+ getCookie('projectID') + '/interpreter/setting');
              },
              restartInterpreter: function (settingId) {
                return $http.put('/api/zeppelin/'+ getCookie('projectID') + '/interpreter/setting/restart/' + settingId);
              },
              notebooks: function () {
                return $http.get('/api/zeppelin/'+ getCookie('projectID') + '/notebook/');
              },
              reloadedNotebooks: function () {
                return $http.get('/api/zeppelin/'+ getCookie('projectID') + '/notebook/reloadedNotebookList');
              },
              deleteNotebook: function (noteId) {
                return $http.delete('/api/zeppelin/'+ getCookie('projectID') + '/notebook/' + noteId);
              },
              createNotebook: function (note) {
                var regReq = {
                  method: 'POST',
                  url: '/api/zeppelin/'+ getCookie('projectID') + '/notebook/new',
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  data: note
                };
                return $http(regReq);
              },
              interpreters: function () {
                return $http.get('/api/zeppelin/'+ getCookie('projectID') + '/interpreter/interpretersWithStatus');
              },
              restart: function () {
                return $http.get('/api/zeppelin/'+ getCookie('projectID') + '/interpreter/restart');
              },
              stopLivySession: function (settingId, sessionId) {
                return $http.delete('/api/zeppelin/'+ getCookie('projectID') + '/interpreter/livy/sessions/delete/' + settingId + '/' + sessionId);
              },
              getLivySessionAppId: function (sessionId) {
                return $http.get('/api/zeppelin/'+ getCookie('projectID') + '/interpreter/livy/sessions/appId/' + sessionId);
              },
              getSparkAppId: function () {
                return $http.get('/api/zeppelin/'+ getCookie('projectID') + '/interpreter/spark/appId');
              }
            };
          }]);

