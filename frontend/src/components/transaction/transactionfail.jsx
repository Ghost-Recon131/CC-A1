import cookie from "js-cookie";
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import {getGlobalState} from "../../utils/globalState";

export default function Component() {
    var navigate = useNavigate();
    var [user, setUser] = useState({});
    var queryParams = new URLSearchParams(window.location.search);

    useEffect(async () => {
        if (cookie.get("user")) {
            user = JSON.parse(cookie.get("user"));
            setUser(user);
        } else {
            navigate("/signin");
        }

        const paymentId = queryParams.get("token");
        await axios.put(getGlobalState("backendDomain2") + "/api/Transactions/cancelPayment?token=" + paymentId);
    }, []);

    return (
        <div>
            <h3 className="text-2xl font-bold text-white shadow-md rounded pt-2 pb-8 mb-4">Purchase Failed</h3>
            <p>Payment via PayPal has failed or been cancelled.</p>
        </div>
    );
}
