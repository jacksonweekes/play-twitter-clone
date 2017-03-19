/**
 * This file contains the ReactJS components which make up the SPA.
 *
 * @author Jackson Cleary
 */

var mountNode = document.getElementById("mountNode");

/******************************************************************************
 * Info section                                                               *
 * Any components shown in the left 'info' panel are in the following section *
 *****************************************************************************/

/**
 * Info Panel - this is what shows the users information on the left of the screen
 */
var InfoPanel = React.createClass({
    /**
     * This handles the event that the user clicks on their own name in the info panel-
     * it causes the app to reset to show the users own posts
     *
     * @param e the click event
     */
    handleSubmit: function (e) {
        e.preventDefault();
        window.resetToUser();
    },
    /**
     * The render function which generates the XML display information
     *
     * @returns the display information for the InfoPanel component
     */
    render: function () {
        return (
            <aside className="col-md-3">
                <section className="user_info">
                    <h1>
                        <a onClick={this.handleSubmit}>
                            {this.props.userDetails.username}
                        </a>
                    </h1>

                    <h2>{this.props.userDetails.email}</h2>
                </section>
            </aside>
        );
    }
});
/***********************
 * END of Info section *
 **********************/


/************************************************************************************
 * Post Section                                                                     *
 * Any components to do with creating or viewing posts are in the following section *
 ***********************************************************************************/

/**
 * PostForm - The text input the user can use to create a new post
 */
var PostForm = React.createClass({
    /**
     * Handles the event the user submits the form, causes post to be submitted to server
     *
     * @param e the event that triggered the submission
     */
    handleSubmit: function (e) {
        e.preventDefault();
        var text = React.findDOMNode(this.refs.text).value.trim();
        if (!text) {
            return;
        }
        this.props.onPostSubmit(text);
        React.findDOMNode(this.refs.text).value = '';
        return;
    },
    /**
     * The render function which generates the XML display information
     * @returns the display information for the PostForm component
     */
    render: function () {
        return (
            <div className="form-controller col-md-6">
                <form onSubmit={this.handleSubmit}>
                    <input type="text" id="postTxtArea" ref="text" placeholder="Tell us your thoughts..."/>
                    <button type="submit" className="btn btn-primary">Post</button>
                </form>
            </div>
        );
    }
});

/**
 * PostItem - A single <li> item which shows the username and message of a post
 */
var PostItem = React.createClass({
    /**
     * Handles event that user clicks on post authors username, has same effect as if
     * the user had searched for the authors name in the search bar
     *
     * @param e the triggering event
     */
    handleAuthorSubmit: function (e) {
        e.preventDefault();
        window.doSearch(this.props.author);
    },
    /**
     * Rather than showing the plain message, this uses a regex to create hyperlinks for
     * each of the tags in the message. Clicking on the link has the same effect as using the
     * search bar to search for the tag
     *
     * @returns the message with all the tags hyperlinked
     */
    getLinkedMessage: function () {
        var message = this.props.children;
        var regex;
        for (var i = 0; i < this.props.tags.length; i++) {
            regex = "[(?<= )#]" + this.props.tags[i] + "\\b";
            var replaceText = "<a onClick=window.doSearch(\"" + this.props.tags[i] + "\")>"
                + "#" + this.props.tags[i] + "<\/a>";
            message = message.replace(new RegExp("#" + this.props.tags[i], 'g'), replaceText);
        }
        return {__html: message};
    },
    /**
     * The render function that generates the XML display information
     * @returns the display information for the PostItem component
     */
    render: function () {
        return (
            <li>
                <a onClick={this.handleAuthorSubmit} className="author">{this.props.author} </a>
                <span dangerouslySetInnerHTML={this.getLinkedMessage()}/>
            </li>
        );
    }
});

/**
 * PostList- a <ul> which contains PostItem components
 */
var PostList = React.createClass({
    /**
     * The render function that generates the XML display information
     * @returns the display information for the PostList component
     */
    render: function () {
        // Map props.data to PostItem components
        var postNodes = this.props.data.map(function (post) {
            return (
                <PostItem key={post.postID} author={'@' + post.username} tags={post.tags}>
                    {post.message}
                </PostItem>
            );
        });
        return (
            <div className="col-md-6">

                <ul className="postList">
                    {postNodes}
                </ul>
            </div>
        );
    }
});
/***********************
 * END of Post section *
 **********************/

/***************************************************************************************
 * Search section                                                                      *
 * Any components used in the search area on the right of the page are in this section *
 **************************************************************************************/

/**
 * SearchForm - The text input and button on right of screen, allows users to search tags or users
 */
var SearchForm = React.createClass({
    /**
     * Handles the event that user submits a search
     * @param e the event that
     */
    handleSubmit: function (e) {
        e.preventDefault();
        var text = React.findDOMNode(this.refs.searchBox).value.trim();
        if (!text) {
            return;
        }
        this.props.onSearch(text);
        React.findDOMNode(this.refs.searchBox).value = '';
        return;
    },
    /**
     * The render function that generates the XML display information
     * @returns the display information for the SearchForm component
     */
    render: function () {
        return (
            <div className="form-controller">
                <form onSubmit={this.handleSubmit}>
                    <input type="text" ref="searchBox" placeholder="Search..."/>
                    <button type="submit" className="btn btn-primary searchBtn">Search</button>

                </form>
            </div>

        );
    }
});

/**
 * SearchPanel - Contains the SearchForm. Not really necessary however it may be required to
 * add other components to this feature in the future, by nesting the SearchForm this will be easier.
 */
var SearchPanel = React.createClass({
    /**
     * The render function that generates the XML display information
     * @returns the display information for the SearchForm component
     */
    render: function () {
        return (
            <div id="searchArea" className="col-md-3">
                <SearchForm onSearch={this.props.onSearch}/>
            </div>
        );
    }
});

/*************************
 * END of Search section *
 ************************/

/*************************************************************************************
 * ChirperApp - This is the main component which is mounted when the page is loaded. *
 * It manages all the other components, passing data from the API.                   *
 ************************************************************************************/
var ChirperApp = React.createClass({
    /**
     * Loads posts from server
     */
    loadPostsFromServer: function () {
        window.loadPosts();
    },
    /**
     * Gets initial data state, in this case just sets data to be an empty array
     *
     * @returns {{data: Array}} empty array
     */
    getInitialState: function () {
        return {data: []}
    },
    /**
     * Called when ChirperApp has successfully loaded, calls loadPostsFromServer then rerenders
     */
    componentDidMount: function () {
        this.loadPostsFromServer();
        rerender();
    },
    /**
     * Handles users submitting posts
     *
     * @param post the message to submit
     */
    handlePostSubmit: function (post) {
        window.sendPost(post);
    },
    /**
     * The render function that generates the XML display information
     * @returns the display information for the ChirperApp component
     */
    render: function () {
        var postForm;

        // Determine whether to show post form depending what feed user is viewing
        if (window.userDetails.username == window.searchTerm) {
            postForm = <PostForm onPostSubmit={this.handlePostSubmit}/>
        } else {
            // Do nothing(Post form only shown if on logged-in users post feed)
            postForm = null;
        }

        return (
            <div className="chirperapp">
                <SearchPanel onSearch={window.doSearch}/>
                <InfoPanel userDetails={window.userDetails}/>
                {postForm}
                <PostList data={window.postData}/>
            </div>
        );
    }
});

/**
 * Causes React to rerender the page
 */
var rerender = function () {
    React.render(<ChirperApp />, mountNode);
}

rerender();