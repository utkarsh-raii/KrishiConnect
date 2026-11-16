package com.krishiconnect.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardStatsDto {

    public static class UserStats {
        private long total;
        private long farmers;
        private long providers;
        private long buyers;

        public UserStats() {}
        public UserStats(long total, long farmers, long providers, long buyers) {
            this.total = total;
            this.farmers = farmers;
            this.providers = providers;
            this.buyers = buyers;
        }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getFarmers() { return farmers; }
        public void setFarmers(long farmers) { this.farmers = farmers; }
        public long getProviders() { return providers; }
        public void setProviders(long providers) { this.providers = providers; }
        public long getBuyers() { return buyers; }
        public void setBuyers(long buyers) { this.buyers = buyers; }
    }

    public static class ListingStats {
        private long total;
        private long active;

        public ListingStats() {}
        public ListingStats(long total, long active) {
            this.total = total;
            this.active = active;
        }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getActive() { return active; }
        public void setActive(long active) { this.active = active; }
    }

    public static class TransactionStats {
        private long total;
        private long pendingReceipts;
        private long completed;
        private BigDecimal totalRevenue;

        public TransactionStats() {}
        public TransactionStats(long total, long pendingReceipts, long completed, BigDecimal totalRevenue) {
            this.total = total;
            this.pendingReceipts = pendingReceipts;
            this.completed = completed;
            this.totalRevenue = totalRevenue;
        }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getPendingReceipts() { return pendingReceipts; }
        public void setPendingReceipts(long pendingReceipts) { this.pendingReceipts = pendingReceipts; }
        public long getCompleted() { return completed; }
        public void setCompleted(long completed) { this.completed = completed; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    }

    public static class BidStats {
        private long total;
        private long accepted;

        public BidStats() {}
        public BidStats(long total, long accepted) {
            this.total = total;
            this.accepted = accepted;
        }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getAccepted() { return accepted; }
        public void setAccepted(long accepted) { this.accepted = accepted; }
    }

    public static class RequirementStats {
        private long total;
        private long open;

        public RequirementStats() {}
        public RequirementStats(long total, long open) {
            this.total = total;
            this.open = open;
        }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getOpen() { return open; }
        public void setOpen(long open) { this.open = open; }
    }

    public static class FeedbackStats {
        private long total;

        public FeedbackStats() {}
        public FeedbackStats(long total) { this.total = total; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
    }

    private UserStats users;
    private ListingStats listings;
    private TransactionStats transactions;
    private BidStats bids;
    private RequirementStats requirements;
    private FeedbackStats feedback;
    private List<UserDto> recentUsers;
    private List<PurchaseResponseDto> recentPurchases;
    private List<MonthlySaleDto> monthlySales;

    public DashboardStatsDto() {}

    public UserStats getUsers() { return users; }
    public void setUsers(UserStats users) { this.users = users; }

    public ListingStats getListings() { return listings; }
    public void setListings(ListingStats listings) { this.listings = listings; }

    public TransactionStats getTransactions() { return transactions; }
    public void setTransactions(TransactionStats transactions) { this.transactions = transactions; }

    public BidStats getBids() { return bids; }
    public void setBids(BidStats bids) { this.bids = bids; }

    public RequirementStats getRequirements() { return requirements; }
    public void setRequirements(RequirementStats requirements) { this.requirements = requirements; }

    public FeedbackStats getFeedback() { return feedback; }
    public void setFeedback(FeedbackStats feedback) { this.feedback = feedback; }

    public List<UserDto> getRecentUsers() { return recentUsers; }
    public void setRecentUsers(List<UserDto> recentUsers) { this.recentUsers = recentUsers; }

    public List<PurchaseResponseDto> getRecentPurchases() { return recentPurchases; }
    public void setRecentPurchases(List<PurchaseResponseDto> recentPurchases) { this.recentPurchases = recentPurchases; }

    public List<MonthlySaleDto> getMonthlySales() { return monthlySales; }
    public void setMonthlySales(List<MonthlySaleDto> monthlySales) { this.monthlySales = monthlySales; }
}
