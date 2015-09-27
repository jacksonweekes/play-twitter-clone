var mountNode = document.getElementById("mountNode");

/******************************************************************************
 * Info section                                                               *
 * Any components shown in the left 'info' panel are in the following section *
 *****************************************************************************/
// This is the panel on the left of the page
var InfoPanel = React.createClass({
    render: function () {
        return (
            <aside className="col-md-3">
                <section className="user_info">
                    <h1>Bob</h1>

                    <h2>bob@example.com</h2>
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
                    <textarea rows="3" id="postTxtArea" ref="text" placeholder="Tell us your thoughts..."/>
                    <button type="submit" className="btn btn-primary">Post</button>
                </form>
            </div>
        );
    }
});

var PostItem = React.createClass({
    render: function () {
        return (
            <li>
                <span className="author">{this.props.author} </span>
                <span>{this.props.children}</span>
            </li>
        );
    }
});

var PostList = React.createClass({
    render: function () {
        var postNodes = this.props.data.map(function (post) {
            return (
                <PostItem author={'@' + post.username}>
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
    render: function () {
        return (
            <form>
                <div className="form-controller">
                    <input type="text" name="searchTerm" placeholder="Search..."/>
                    <button type="submit" className="btn btn-primary searchBtn">Search</button>
                </div>
            </form>

        );
    }
});
var SearchPanel = React.createClass({
    render: function () {
        return (
            <div id="searchArea" className="col-md-3">
                <SearchForm />
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
        $.ajax({
            url: this.props.url,
            dataType: 'json',
            cache: false,
            success: function (data) {
                this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function () {
        return {data: []}
    },
    componentDidMount: function () {
        this.loadPostsFromServer();
        setInterval(this.loadPostsFromServer, this.props.pollInterval);
    },
    handlePostSubmit: function (post) {
        $.ajax({
            url: this.props.postUrl,
            contentType: 'text/plain',
            type: 'POST',
            data: post,
            success: function (data) {
                this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    render: function () {
        return (
            <div className="twatterapp">
                <SearchPanel />
                <InfoPanel />
                <PostForm onPostSubmit={this.handlePostSubmit} />
                <PostList data={this.state.data}/>
            </div>
        );
    }
});

React.render(<TwatterApp url="/api/users/bob" postUrl="/api/postmessage" pollInterval={2000}/>, mountNode);