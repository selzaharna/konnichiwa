var SlackThis = (function () {
    
    var cfg = {
            api_url : "https://slack.com/api/",
            api_token : "", 
            api_endpoints : {
                "users" : "users.list",
                "chat"  : "chat.postMessage",
                "directmessage"  : "im.open"
            },
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
                    if (members.hasOwnProperty(key)) {
                        user = {
                            id    : members[key].id,
                            name  : (members[key].real_name) ? members[key].real_name : members[key].name,
                            email : members[key].profile.email,
                            title : (members[key].profile.title) ? members[key].profile.title : 'I have no title',
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
                //console.log('results');
                //console.log(xmlhttp.responseText);
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
        sendDirectMessage : sendDirectMessage
    };

})();