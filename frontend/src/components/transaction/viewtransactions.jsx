import { getGlobalState, setGlobalState } from "utils/globalState";
import cookie from "js-cookie";
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function Component() {
    var navigate = useNavigate();
    var [user, setUser] = useState({});
    var [purchaseHistory, setPurchaseHistory] = useState([]);

    useEffect(() => {
        if (cookie.get("user")) {
            user = JSON.parse(cookie.get("user"));
            setUser(user);

        } else {
            navigate("/signin");
        }

        // Do axios call here
        async function axiosGet() {
            var res = await axios.get(getGlobalState("backendDomain2") + "/api/Transactions/getUserPurchases?userID=" + user.id);
            setPurchaseHistory(res.data);
            console.log("Transactions: "+ JSON.stringify(res));
        }
        axiosGet();
    }, []);

    return (
        <div>
            <h1 className="text-2xl font-bold text-white shadow-md rounded pt-2 pb-8 mb-4">Your purchase history</h1>
            {purchaseHistory.map((purchaseHistory) => (
                <div key={purchaseHistory.transactionID}>
                    <p>Web-store Transaction ID: {purchaseHistory.transactionID}</p>
                    <p>Price: ${purchaseHistory.price} {purchaseHistory.currency}</p>
                    <p>Time of purchase: {purchaseHistory.timeOfPurchase}</p>
                    <p>Seller's ID: {purchaseHistory.sellerID}</p>
                </div>
            ))}
        </div>
    );
}
