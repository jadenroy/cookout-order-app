import { Routes, Route } from "react-router-dom";

import CreateCookoutView from "./views/CreateCookoutView/CreateCookoutView";
import CookoutsListView from "./views/CookoutsListView/CookoutsListView";
import CookoutDetailView from "./views/CookoutDetailView/CookoutDetailView";
import CreateMenuView from "./views/CreateMenuView/CreateMenuView";
import PlaceOrderView from "./views/PlaceOrderView/PlaceOrderView";
import OrdersListView from "./views/OrdersListView/OrdersListView";

import HomeView from "./views/HomeView/HomeView";
import LoginView from "./views/LoginView/LoginView";
import LogoutView from "./views/LogoutView";
import RegisterView from "./views/RegisterView/RegisterView";
import UserProfileView from "./views/UserProfileView/UserProfileView";
import InvitationsListView from "./views/InvitationsListView/InvitationsListView";
import SendInvitationsView from "./views/SendInvitationsView/SendInvitationsView";

import MainNav from "./components/MainNav/MainNav";
import ProtectedRoute from "./components/ProtectedRoute";

export default function App() {
  return (
    <div id="app">
      <MainNav />

      <main id="main-content">
        <Routes>
          {/* ---------------- PUBLIC ROUTES ---------------- */}
          <Route path="/" element={<HomeView />} />
          <Route path="/login" element={<LoginView />} />
          <Route path="/logout" element={<LogoutView />} />
          <Route path="/register" element={<RegisterView />} />

          {/* ---------------- PROTECTED ROUTES ---------------- */}
          <Route
            path="/userProfile"
            element={
              <ProtectedRoute>
                <UserProfileView />
              </ProtectedRoute>
            }
          />

          <Route
            path="/cookout/new"
            element={
              <ProtectedRoute>
                <CreateCookoutView />
              </ProtectedRoute>
            }
          />

          <Route
            path="/cookouts"
            element={
              <ProtectedRoute>
                <CookoutsListView />
              </ProtectedRoute>
            }
          />

          <Route
            path="/cookout/:id"
            element={
              <ProtectedRoute>
                <CookoutDetailView />
              </ProtectedRoute>
            }
          />

          {/* CREATE MENU */}
          <Route
            path="/cookout/:id/menu/new"
            element={
              <ProtectedRoute>
                <CreateMenuView />
              </ProtectedRoute>
            }
          />

          {/* PLACE ORDER (ATTENDEE) */}
          <Route
            path="/cookout/:id/order"
            element={
              <ProtectedRoute>
                <PlaceOrderView />
              </ProtectedRoute>
            }
          />

          {/* ORDERS LIST (CHEF / HOST) */}
          <Route
            path="/cookout/:id/orders"
            element={
              <ProtectedRoute>
                <OrdersListView />
              </ProtectedRoute>
            }
          />

          {/* INVITATIONS */}
          <Route
            path="/invitations"
            element={
              <ProtectedRoute>
                <InvitationsListView />
              </ProtectedRoute>
            }
          />

          <Route
            path="/cookout/:id/invite"
            element={
              <ProtectedRoute>
                <SendInvitationsView />
              </ProtectedRoute>
            }
          />
        </Routes>
      </main>
    </div>
  );
}
