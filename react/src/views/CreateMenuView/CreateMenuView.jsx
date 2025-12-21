import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import MenuService from "../../services/MenuService";
import styles from "./CreateMenuView.module.css";

export default function CreateMenuView() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [items, setItems] = useState([]);
  const [newItemNames, setNewItemNames] = useState([""]);
  const [error, setError] = useState("");

  const [savingId, setSavingId] = useState(null);
  const [savedId, setSavedId] = useState(null);

  useEffect(() => {
    MenuService.getMenuByCookoutId(id)
      .then(res => {
        if (res.data?.items) {
          setItems(res.data.items);
        }
      })
      .catch(() => {});
  }, [id]);

  function handleExistingChange(itemId, value) {
    setItems(items.map(i =>
      i.id === itemId ? { ...i, itemName: value } : i
    ));
  }

  function saveItem(itemId, itemName) {
  setSavingId(itemId);
  setSavedId(null);

    MenuService.updateMenuItem(itemId, itemName)
    .then(() => {
      setSavedId(itemId);
      setTimeout(() => setSavedId(null), 1500);
    })
    .catch(() => setError("Failed to update item"))
    .finally(() => setSavingId(null));
  }

  function deleteItem(itemId) {
    MenuService.deleteMenuItem(itemId)
      .then(() => {
        setItems(items.filter(i => i.id !== itemId));
      })
      .catch(() => setError("Failed to delete item"));
  }



  function handleNewChange(index, value) {
    const copy = [...newItemNames];
    copy[index] = value;
    setNewItemNames(copy);
  }

  function addNewItem() {
    setNewItemNames([...newItemNames, ""]);
  }

  function removeNewItem(index) {
    setNewItemNames(newItemNames.filter((_, i) => i !== index));
  }

  function saveNewItems(e) {
    e.preventDefault();

    const filtered = newItemNames.filter(i => i.trim() !== "");
    if (filtered.length === 0) {
      setError("Add at least one menu item.");
      return;
    }

    MenuService.createMenu({
      cookoutId: Number(id),
      itemNames: filtered
    })
      .then(() => navigate(`/cookout/${id}`))
      .catch(() => setError("Failed to save menu"));
  }

  return (
    <div className={styles.container}>
      <h1>Manage Menu</h1>

      {/* EXISTING ITEMS */}
      {items.length > 0 && (
        <>
          <h3>Existing Items</h3>
          {items.map(item => (
            <div key={item.id} className={styles.row}>
              <input
                value={item.itemName}
                onChange={e => handleExistingChange(item.id, e.target.value)}
                onBlur={() => saveItem(item.id, item.itemName)}
                disabled={savingId === item.id}
              />
              {savingId === item.id && <span className={styles.saving}>Saving</span>}
              {savedId === item.id && <span className={styles.saved}>Saved</span>}
              <button
                type="button"
                onClick={() => deleteItem(item.id)}
                disabled={savingId === item.id}
              >
                🗑
              </button>
            </div>
          ))}
        </>
      )}

      {/* ADD NEW ITEMS */}
      <h3>Add New Items</h3>

      <form onSubmit={saveNewItems}>
        {newItemNames.map((item, i) => (
          <div key={i} className={styles.row}>
            <input
              value={item}
              onChange={e => handleNewChange(i, e.target.value)}
              placeholder="Menu item"
            />
            {newItemNames.length > 1 && (
              <button type="button" onClick={() => removeNewItem(i)}>✕</button>
            )}
          </div>
        ))}

        <button type="button" onClick={addNewItem} className={styles.addBtn}>
          + Add Item
        </button>

        <button type="submit" className={styles.submitBtn}>
          Save New Items
        </button>

        {error && <p className={styles.error}>{error}</p>}
      </form>
    </div>
  );
}
