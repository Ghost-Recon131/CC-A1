import cookie from "js-cookie";
import { useNavigate, Link } from "react-router-dom";

export default function Component() {
  const navigate = useNavigate();

  function accountList(event) {
    event.preventDefault();
    if (event.target.value === "signOut") {
      cookie.remove("user");
      navigate("/");
    } else if (event.target.value === "newListing") {
      navigate("/newlisting");
    }else if (event.target.value === "viewAccountInfo") {
      navigate("/viewAccountInfo");
    }else if (event.target.value === "viewtransactions") {
      navigate("/viewtransactions");
    }

  }

  // Convert from string to object
  var user = {};
  if (cookie.get("user")) {
    user = JSON.parse(cookie.get("user"));
  }
  return (
    <nav className="font-sans flex flex-col text-center sm:flex-row sm:text-left sm:justify-between py-4 px-6  shadow sm:items-baseline w-full">
      <div className="mb-2 sm:mb-0">
        <Link
          to="/"
          className="text-2xl no-underline text-grey-darkest hover:text-blue-dark"
        >
          Home
        </Link>
      </div>
      <div>
        {user.username ? (
          <div className="rounded hover:font-medium hover:text-gray-400 md:mx-2">
            <select
              className="custom-select text-capitalize bg-black"
              onChange={accountList}
              value=""
            >
              <option hidden>{user.username}</option>
              <option value="newListing">New Listing</option>
              <option value="viewAccountInfo">View Account info</option>
              <option value="viewtransactions">View purchases</option>
              <option value="signOut">Sign Out</option>
            </select>
          </div>
        ) : (
          <Link
            to="/signin"
            className="text-lg no-underline text-grey-darkest hover:text-blue-dark ml-2"
          >
            Sign in
          </Link>
        )}
      </div>
    </nav>
  );
}
