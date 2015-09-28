/**
 * Created by jackson on 28/09/15.
 */

(function() {
    window.userDetails = "";

    window.getUserDetails = function() {
        //var server = "localhost:9000";
        var path = "http://" + server + "/api/userdetails";
        $.ajax({
            url: path,
            dataType: 'json',
            cache: false,
            success: function (data) {
                this.userDetails = data;
                //this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(path, status, err.toString());
            }.bind(this)
        });
    };

    window.sendPost = function(post) {
        $.ajax({
            url: this.props.postUrl,
            contentType: 'text/plain',
            type: 'POST',
            data: post,
            success: function (data) {
                // Do nothing
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    }

}).call(this);