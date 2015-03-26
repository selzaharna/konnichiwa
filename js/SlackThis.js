var SlackThis = (function () {
 
    var cfg = {
            api_url : "https://slack.com/api/",
            api_token : "xoxp-2194787930-4146388799-4203794499-4df53e",
            api_endpoints : {
                "users" : "users.list",
                "chat"  : "chat.postMessage"
            }
    },

    sendMessage = function(params) {

        //send Slack a message 
        var channel  = (params.channel) ? params.channel : "U044ABEPH",
            username = (params.username) ? params.username : "mkitzman",
            text     = (params.text) ? params.text : "Konnichiwa!",
            query_params = {
                            "channel" : channel, 
                            "username" : username, 
                            "text" : text
            },
            results,
            messageSentStatus,

        //Make ajax call to send message
        results =  get( cfg.api_endpoints.chat, query_params );

        if(results.ok) {
            messageSentStatus = true;
        } else {
            messageSentStatus = false;
        }

        return {
            messageSent : messageSentStatus
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
                        //console.log(members[key]);
                        user = {
                            id    : members[key].id,
                            name  : (members[key].real_name) ? members[key].real_name : members[key].name,
                            email : members[key].profile.email,
                            title : (members[key].profile.title) ? members[key].profile.title : 'I have no title',
                            image : members[key].profile.image_192
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
        sendMessage : sendMessage
    };

})();

