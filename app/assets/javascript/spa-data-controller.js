/**
 * Created by jackson on 28/09/15.
 */

(function() {
    // Holds the search type('users' or 'tags') and search term(ie. username)
    window.searchType = 'users';
    window.searchTerm = this.userDetails.username;

    window.postData = [];

    var websocket = null;

    window.setSearchType = function(type) {
        this.searchType = type;
    }

    window.setSearchTerm = function(term) {
        this.searchTerm = term;
    };

    window.resetToUser = function() {
        setSearchTerm(this.userDetails.username);
        setSearchType('users');
        loadPosts();
        rerender();
    }

    // Makes AJAX POST request(using jQuery) to server to post a new message
    // Called when user submits the new post form.
    window.sendPost = function(post) {
        var path = "/api/postmessage";
        $.ajax({
            url: path,
            contentType: 'text/plain',
            type: 'POST',
            data: post,
            success: function (data) {
                // Do nothing
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(path, status, err.toString());
            }.bind(this)
        });
    };

    // Loads initial set of posts and sets up new websocket
    window.loadPosts = function() {
        // Close current websocket if it exists
        if(websocket != null) {
            websocket.close();
        }

        // Make ajax call using jQuery to get initial set of(up to) 30 posts
        var path = "/api/" + this.searchType + "/" + this.searchTerm;
        $.ajax({
            url: path,
            dataType: 'json',
            cache: false,
            success: function (data) {
                this.postData = data;
                rerender();
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(path, status, err.toString());
            }.bind(this)
        });

        this.getSocket();
    }

    window.doSearch = function(searchValue) {
        // Check that searchValue is not empty
        if(!searchValue) {
            return;
        }

        // Determine whether search is for user or tag and set searchType
        // If first character is @, search for users
        if(searchValue.charAt(0) == '@') {
            setSearchType('users');
            // Dont include the @ character in the search term
            setSearchTerm(searchValue.substring(1, searchValue.length));
        } else {
            setSearchType('tags');
            // In case use has used # character, exclude  it
            if(searchValue.charAt(0) == '#') {
                setSearchTerm(searchValue.substring(1, searchValue.length));
            } else {
                // Otherwise use whole searchValue
                setSearchTerm(searchValue);
            }
        }

        // Load new set of posts
        loadPosts();

        rerender();
    }

    // Creates the web socket connection
    window.getSocket = function() {
        websocket = new WebSocket("ws://" + window.location.host
            + "/ws?searchTerm=" + this.searchTerm + "&searchType=" + this.searchType);
        return websocket.onmessage = function(msg) {
            var json;
            json = JSON.parse(msg.data);
            console.log("recieved message: " + msg.data);
            window.postData.unshift(json);
            return rerender();
        }
    }

}).call(this);