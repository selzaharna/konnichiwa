var SlackThis = (function () {

    

    var cfg = {
            api_url : "https://slack.com/api/",
            api_endpoints : {
                "users" : "users.list",
                "chat"  : "chat.postMessage",
                "directmessage"  : "im.open"
            },
            'logged_out_error' : {
                ok : false,
                'error' : 'User Not logged in'
            },
            cookie_name : "api_token",
            query_token_name : "access_token"
    },

    isLoggedIn = function() {

        if(getUserCookie()) {
            return true
        } else if (getAPITokenInURL()) {
            setCookie(cfg.api_token, getAPITokenInURL);
            return true;
        } else {
            return false;
        }

    },

    setCookie = function(cname, cvalue) {
        var d = new Date();
        var expireDays = 90;

        d.setTime(d.getTime() + (expireDays*24*60*60*1000));
        var expires = "expires="+d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    },

    getAPITokenInURL = function() {

        var regex = new RegExp("[\\?&]" + cfg.query_token_name + "=([^&#]*)"),
        results = regex.exec(location.search);

        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },

    getUserCookie = function(){

        var regexp = new RegExp("(?:^" + cfg.cookie_name + "|;\s*"+ cfg.cookie_name + ")=(.*?)(?:;|$)", "g");
        var result = regexp.exec(document.cookie);
        return (result === null) ? null : result[1];

    },


    openDirectMessage = function(userId) {
        /**
        ** Opens up a Chat room with the person you want to direct message with
        ** Param: User: User Id (U044ABEPH) 
        ** Returns json blob of data that will include the chat room id
        **/
        return get( cfg.api_endpoints.directmessage, {user : userId} );
    }

    sendMessage = function(query_params) {
        /**
        ** Sends a Message to a user
        ** Param: query_params - json blob with the required values
        **  "channel" = the channel id (U044ABEPH) that you want to post to
        **  "username" : The name of the person sending the message (Jane Doe, JohnDoe )
        **  "text" : text of message
        ** Returns json blob of the message sent and a response
        **/
        return get( cfg.api_endpoints.chat, query_params );

    }

    sendDirectMessage = function(message_cfg) {

        /**
        ** To send a direct message you first need to make sure a IM room is open
        ** then use the returned channel id to send the direct message to:
        **/
        if(!isLoggedIn()) {
            return cfg.logged_out_error;
        }

        var directMessage = openDirectMessage(message_cfg.toUserId);
        
        if(directMessage.ok) {
            
            ///Use the channel id value returned from openDirectMessage
            //Send message
            return sendMessage({
                channel : directMessage.channel.id,
                username : message_cfg.fromUserName,
                text : message_cfg.text
            });

        } else {

             return directMessage; //This will have the error in the json blob

        }

    },

    getUsers = function (dump) {

        if(!isLoggedIn()) {
            return cfg.logged_out_error;
        }

        //get user list
        var results = get(cfg.api_endpoints.users),
            userList = [],
            user = {},
            members;

        if(results.ok) {

            members = results.members;

            if(typeof(dump) !== 'undefined' ) {

                return members;

            } else {

                for (key in members) {
                    if (members.hasOwnProperty(key) && !members[key].is_bot) {
                        user = {
                            id    : members[key].id,
                            name  : (members[key].real_name) ? members[key].real_name : members[key].name,
                            email : members[key].profile.email,
                            title : (members[key].profile.title) ? members[key].profile.title : '',
                            image : (members[key].profile.image_original) ? members[key].profile.image_original : members[key].profile.image_192
                        }

                        userList.push(user);
                    }
                }
            }

            return userList;

        } else {

            return results.error
        }
    },

    get = function (url, query_params) {

        var xmlhttp, 
            results,
            url,
            querystring = '',
            key;

        //build query string
        if(query_params) {
            for (key in query_params) {
                if (query_params.hasOwnProperty(key)) {
                    querystring = querystring + "&" + key + "=" + query_params[key];
                }
            }
        }

        //Build url
        url = encodeURI(cfg.api_url + url + "?token=" + cfg.api_token + querystring);

        //Make request
        xmlhttp=new XMLHttpRequest();
        xmlhttp.onreadystatechange=function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                results = JSON.parse(xmlhttp.responseText);
            }
        }

        xmlhttp.open("GET",url,false);
        xmlhttp.send();

        return results;

    };

    // public methods
    return {
        users: getUsers,
        sendDirectMessage : sendDirectMessage,
        isLoggedIn : isLoggedIn,
    };

})();