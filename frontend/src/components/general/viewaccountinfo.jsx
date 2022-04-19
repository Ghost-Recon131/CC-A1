import { getGlobalState, setGlobalState } from "utils/globalState";
import cookie from "js-cookie";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function Component() {
    var navigate = useNavigate();
    // initial state
    var [accountInfo, setAccountInfo] = useState([]);


    var [user, setUser] = useState({});


    useEffect(() => {

        // Verify user is logged in
        if (cookie.get("user")) {
            user = JSON.parse(cookie.get("user"));
            setUser(user);

        } else {
            navigate("/signin");
        }

        async function axiosPost() {
            var res = await axios.get(
                getGlobalState("backendDomain") + "/api/AccountInfo/getAccountInfo" + user.id
            );
            setAccountInfo(res.data);
        }
        axiosPost();
    }, []);

    return (
        <div>
            <h3 className="text-2xl font-bold text-white shadow-md rounded pt-2 pb-8 mb-4">Account Info</h3>
            <p>Account ID: {user.id}</p>
            <p>Email / username: {user.username}</p>
            <p>DOB: {accountInfo.dob}</p>
            <p>Phone Number: {accountInfo.phone}</p>
            <p>Address: {accountInfo.address}</p>
        </div>
    );
}
