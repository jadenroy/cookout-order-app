import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import MenuService from "../../services/MenuService";
import OrderService from "../../services/OrderService";
import styles from "./PlaceOrderView.module.css";

export default function PlaceOrderView() {
  const { id: cookoutId } = useParams();
  const navigate = useNavigate();

  const [menu, setMenu] = useState(null);
  const [quantities, setQuantities] = useState({});
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    MenuService.getMenuByCookoutId(cookoutId)
      .then(res => setMenu(res.data))
      .catch(() => setError("Menu not available."));
  }, [cookoutId]);

  function handleQuantityChange(itemId, value) {
    setQuantities(prev => ({
      ...prev,
      [itemId]: Number(value)
    }));
  }

  function handleSubmit(e) {
    e.preventDefault();
    setError(null);

    const itemIds = [];
    const itemQuantities = [];

    Object.entries(quantities).forEach(([itemId, qty]) => {
      if (qty > 0) {
        itemIds.push(Number(itemId));
        itemQuantities.push(qty);
      }
    });

    if (itemIds.length === 0) {
      setError("Please select at least one item.");
      return;
    }

    setSubmitting(true);

    OrderService.createOrder({
      cookoutId: Number(cookoutId),
      itemIds,
      itemQuantities
    })
      .then(() => {
        navigate(`/cookout/${cookoutId}`);
      })
      .catch(() => {
        setError("Failed to place order.");
      })
      .finally(() => setSubmitting(false));
  }

  if (!menu) {
    return <p className={styles.loading}>Loading menu…</p>;
  }

  return (
    <div className="user-profile-bg">
      <div className={styles.container}>
        <h2 className={styles.title}>Place Your Order</h2>

        {error && <p className={styles.error}>{error}</p>}

        <form onSubmit={handleSubmit} className={styles.form}>
          {menu.items.map(item => (
            <div key={item.id} className={styles.itemRow}>
              <span className={styles.itemName}>{item.itemName}</span>

              <input
                type="number"
                min="0"
                value={quantities[item.id] || ""}
                onChange={e =>
                  handleQuantityChange(item.id, e.target.value)
                }
                className={styles.qtyInput}
              />
            </div>
          ))}

          <button
            type="submit"
            disabled={submitting}
            className={styles.submitBtn}
          >
            {submitting ? "Placing Order…" : "Submit Order"}
          </button>
        </form>
      </div>
    </div>
  );
}
