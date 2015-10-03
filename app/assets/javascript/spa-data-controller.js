/**
 * This file handles the interaction between the ReactJS app and the server
 */

(function() {
    // Holds the search type('users' or 'tags') and search term(ie. username)
    window.searchType = 'users';
    window.searchTerm = this.userDetails.username;

    // The current Posts
    window.postData = [];

    // Our websocket
    var websocket = null;

    /**
     * Sets the searchType('users' or 'tags')
     *
     * @param type the search type
     */
    window.setSearchType = function(type) {
        this.searchType = type;
    }

    /**
     * Sets the searchTerm(ie. a username or a tag)
     *
     * @param term the searchTerm
     */
    window.setSearchTerm = function(term) {
        this.searchTerm = term;
    };

    /**
     * Resets the page to show the logged in users posts
     */
    window.resetToUser = function() {
        setSearchTerm(this.userDetails.username);
        setSearchType('users');
        loadPosts();
        rerender();
    }

    /**
     * Makes AJAX POST request(using jQuery) to server to post a new message
     * Called when user submits the new post form.
     *
     * @param post the message to post to the server
     */
    window.sendPost = function(post) {
        // the path to the api endpoint
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

    /**
     * Loads initial set of posts and sets up new websocket
     */
    window.loadPosts = function() {
        // Close current websocket if it exists
        if(websocket != null) {
            websocket.close();
        }

        // create the query path according to our searchType and searchTerm
        var path = "/api/" + this.searchType + "/" + this.searchTerm;

        // Make ajax call using jQuery to get initial set of(up to) 30 posts
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

        // create a new websocket connection with new details
        this.getSocket();
    }

    /**
     * Gets new set of posts based on the searchValue. If first character is a @ it searches for users,
     * if # or no preceding character, searches for tags
     *
     * @param searchValue the value containing the search information
     */
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

        // Get React to rerender page
        rerender();
    }

    /**
     * Creates a websocket connection using current searchType and searchTerm
     *
     * @returns a new websocket connection
     */
    window.getSocket = function() {
        // Create new websocket
        websocket = new WebSocket("ws://" + window.location.host
            + "/ws?searchTerm=" + this.searchTerm + "&searchType=" + this.searchType);

        // Define websockets behaviour when a message is received
        return websocket.onmessage = function(msg) {
            var json;
            // Extract json data from the message
            json = JSON.parse(msg.data);
            window.postData.unshift(json);
            return rerender();
        }
    }

}).call(this);