var mountNode = document.getElementById("mountNode");

/******************************************************************************
 * Info section                                                               *
 * Any components shown in the left 'info' panel are in the following section *
 *****************************************************************************/
// This is the panel on the left of the page
var InfoPanel = React.createClass({
    handleSubmit: function(e) {
        e.preventDefault();
        window.resetToUser();
    },
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
var PostForm = React.createClass({
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

var PostItem = React.createClass({
    handleAuthorSubmit: function(e) {
        e.preventDefault();
        window.doSearch(this.props.author);
    },
    getLinkedMessage: function() {
        var message = this.props.children;
        var regex;
        for(var i = 0; i < this.props.tags.length; i++) {
            regex = "[(?<= )#]" + this.props.tags[i] + "\\b";
            var replaceText = "<a onClick=window.doSearch(\"" + this.props.tags[i] + "\")>"
                + "#" + this.props.tags[i] + "<\/a>";
            message = message.replace(new RegExp("#" + this.props.tags[i], 'g'), replaceText);
        }
        return {__html: message};
    },
    render: function () {
        return (
            <li>
                <a onClick={this.handleAuthorSubmit} className="author">{this.props.author} </a>
                <span dangerouslySetInnerHTML={this.getLinkedMessage()} />
            </li>
        );
    }
});

var PostList = React.createClass({
    render: function () {
        var postNodes = this.props.data.map(function (post) {
            return (
                <PostItem author={'@' + post.username} tags={post.tags}>
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
var SearchForm = React.createClass({
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
var SearchPanel = React.createClass({
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
 * TwatterApp - This is the main component which is mounted when the page is loaded. *
 * It manages all the other components, passing data from the API.                   *
 ************************************************************************************/
var TwatterApp = React.createClass({
    loadPostsFromServer: function () {
        window.loadPosts();
    },
    getInitialState: function () {
        return {data: []}
    },
    componentDidMount: function () {
        this.loadPostsFromServer();
        rerender();
    },
    handlePostSubmit: function (post) {
        window.sendPost(post);
    },
    render: function () {
        var postForm;

        // Determine whether to show post form depending what feed user is viewing
        if (window.userDetails.username == window.searchTerm) {
            postForm = <PostForm onPostSubmit={this.handlePostSubmit} />
        } else {
            // Do nothing(Post form only shown if on logged-in users post feed)
            postForm = null;
        }

        return (
            <div className="twatterapp">
                <SearchPanel onSearch={window.doSearch}/>
                <InfoPanel userDetails={window.userDetails}/>
                {postForm}
                <PostList data={window.postData} />
            </div>
        );
    }
});

var rerender = function () {
    React.render(<TwatterApp />, mountNode);
}

rerender();