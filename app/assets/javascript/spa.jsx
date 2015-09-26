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
var PostItem = React.createClass({
    render: function () {
        return (
            <li>
                <span className="author">{this.props.author} </span>
                <span>{this.props.message}</span>
            </li>
        );
    }
});

var PostList = React.createClass({
    render: function () {
        return (
            <div className="col-md-6">
                <ul className="postList">
                    <PostItem author="@jerry" message="oh yea"/>
                    <PostItem author="@ralph" message="gloves rule!"/>
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
                    <input type="text" name="searchTerm" placeholder="Search..." />
                </div>
                <button type="submit" className="btn btn-primary searchBtn">Search</button>
            </form>

        );
    }
});
var SearchPanel = React.createClass({
    render: function () {
        return (
            <div id="searchArea" class="col-md-3">
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
    render: function () {
        return (
            <div className="twatterapp">
                <h1>TwatterApp</h1>
                <SearchPanel />
                <InfoPanel />
                <PostList />
            </div>
        );
    }
});

React.render(<TwatterApp />, mountNode);