import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import OrderService from "../../services/OrderService";
import MenuService from "../../services/MenuService";
import styles from "./OrdersListView.module.css";

export default function OrdersListView() {
  const { id: cookoutId } = useParams();

  const [orders, setOrders] = useState([]);
  const [menuMap, setMenuMap] = useState({});
  const [loading, setLoading] = useState(true);

  function loadOrders() {
    return OrderService.getOrdersByCookout(cookoutId).then(res => {
      setOrders(res.data.filter(o => !o.finished));
    });
  }

  function loadMenu() {
    return MenuService.getMenuByCookoutId(cookoutId).then(res => {
      const map = {};
      res.data.items.forEach(item => {
        map[item.id] = item.itemName;
      });
      setMenuMap(map);
    });
  }

  useEffect(() => {
    let interval;

    Promise.all([loadOrders(), loadMenu()])
      .finally(() => setLoading(false));

    // 🔁 AUTO REFRESH EVERY 5 SECONDS
    interval = setInterval(loadOrders, 5000);

    return () => clearInterval(interval);
  }, [cookoutId]);

  function handleFinishOrder(orderId) {
    OrderService.finishOrder(orderId)
      .then(() => loadOrders());
  }

  if (loading) {
    return <p className={styles.loading}>Loading orders…</p>;
  }

  return (
    <div className="user-profile-bg">
      <div className={styles.container}>
        <h2 className={styles.title}>Incoming Orders</h2>

        {orders.length === 0 && (
          <p className={styles.empty}>No active orders.</p>
        )}

        <div className={styles.grid}>
          {orders.map(order => (
            <div key={order.orderId} className={styles.orderCard}>
              <h4>Order #{order.orderId}</h4>

              <p className={styles.attendee}>
                👤 {order.attendeeUsername}
              </p>

              <ul>
                {order.items.map(item => (
                  <li key={item.id}>
                    {menuMap[item.itemId] || "Item"} × {item.quantity}
                  </li>
                ))}
              </ul>

              <button
                className={styles.finishBtn}
                onClick={() => handleFinishOrder(order.orderId)}
              >
                ✅ Mark Finished
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
