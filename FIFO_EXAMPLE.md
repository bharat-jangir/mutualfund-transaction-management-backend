# FIFO (First In, First Out) Implementation Guide

## 🎯 What is FIFO?

**FIFO (First In, First Out)** is a method used in mutual fund transactions to determine which units are sold when an investor redeems their investment. The first units purchased are assumed to be the first ones sold.

## 📊 Example Scenario

### Purchase History:
1. **Jan 1, 2024**: 100 units @ ₹10 each = ₹1,000
2. **Feb 1, 2024**: 50 units @ ₹12 each = ₹600
3. **Mar 1, 2024**: 75 units @ ₹11 each = ₹825

### Redemption Request:
- **Apr 1, 2024**: 80 units @ ₹15 each = ₹1,200

### FIFO Calculation:
1. **First 80 units** come from the **first purchase** (Jan 1)
2. **Cost Basis**: 80 × ₹10 = ₹800
3. **Sale Proceeds**: 80 × ₹15 = ₹1,200
4. **Capital Gain**: ₹1,200 - ₹800 = ₹400

## 🚀 API Endpoints

### 1. Create Purchase Lot
```http
POST /api/fifo/create-purchase-lot
```
**Parameters:**
- `pan`: ABCDE1234F
- `schemeId`: 1
- `folioNo`: FOL001
- `purchaseDate`: 2024-01-01
- `purchaseNav`: 10.0000
- `purchaseUnits`: 100.0000
- `purchaseAmount`: 1000.00

### 2. Calculate FIFO for Redemption
```http
POST /api/fifo/calculate-redemption
```
**Parameters:**
- `pan`: ABCDE1234F
- `schemeId`: 1
- `folioNo`: FOL001
- `redemptionUnits`: 80.0000
- `redemptionNav`: 15.0000
- `redemptionDate`: 2024-04-01

**Response:**
```json
{
  "success": true,
  "message": "FIFO calculation completed",
  "data": {
    "pan": "ABCDE1234F",
    "folioNo": "FOL001",
    "schemeId": 1,
    "redemptionDate": "2024-04-01",
    "redemptionUnits": 80.0000,
    "redemptionNav": 15.0000,
    "redemptionAmount": 1200.00,
    "totalCostBasis": 800.00,
    "capitalGain": 400.00,
    "capitalGainPercentage": 50.00,
    "lotBreakdowns": [
      {
        "lotId": 1,
        "purchaseDate": "2024-01-01",
        "purchaseNav": 10.0000,
        "unitsUsed": 80.0000,
        "costBasis": 800.00,
        "gainLoss": 400.00
      }
    ]
  }
}
```

### 3. Process Redemption (Updates Lots)
```http
POST /api/fifo/process-redemption
```
**Parameters:** Same as calculate-redemption
**Effect:** Updates the remaining units in lots

### 4. Get Current Lots
```http
GET /api/fifo/lots/{pan}/{schemeId}
```

### 5. Get Remaining Units
```http
GET /api/fifo/remaining-units/{pan}/{schemeId}
```

### 6. Get Cost Basis
```http
GET /api/fifo/cost-basis/{pan}/{schemeId}
```

### 7. Calculate Unrealized Gain/Loss
```http
GET /api/fifo/unrealized-gain-loss/{pan}/{schemeId}?currentNav=15.0000
```

## 🔄 Automatic FIFO Integration

### Excel Upload Integration
When you upload transactions via Excel:
1. **Purchase transactions** automatically create FIFO lots
2. **Redemption transactions** can be processed using FIFO
3. **Lots are tracked** with remaining units

### Example Excel Upload:
```csv
PAN,TaxStatus,TxnType,DOB,TxnDate,Units,Amount,NAV,SchemeId,FolioNo
ABCDE1234F,INDIVIDUAL,PURCHASE,1990-01-01,2024-01-01,100.0000,1000.00,10.0000,1,FOL001
ABCDE1234F,INDIVIDUAL,PURCHASE,1990-01-01,2024-02-01,50.0000,600.00,12.0000,1,FOL001
ABCDE1234F,INDIVIDUAL,REDEMPTION,1990-01-01,2024-04-01,80.0000,1200.00,15.0000,1,FOL001
```

## 📈 Business Benefits

### 1. **Tax Compliance**
- Accurate capital gains calculation
- Proper cost basis tracking
- Regulatory compliance

### 2. **Portfolio Management**
- Track performance of different purchase lots
- Understand investment timing impact
- Optimize redemption strategies

### 3. **Financial Reporting**
- Accurate profit/loss calculations
- Detailed transaction history
- Audit trail for compliance

## 🛠️ Technical Implementation

### Database Schema
```sql
CREATE TABLE transaction_lots (
    lot_id INT PRIMARY KEY AUTO_INCREMENT,
    pan VARCHAR(10) NOT NULL,
    scheme_id INT NOT NULL,
    folio_no VARCHAR(30) NOT NULL,
    purchase_date DATE NOT NULL,
    purchase_nav DECIMAL(10,4) NOT NULL,
    purchase_units DECIMAL(18,4) NOT NULL,
    purchase_amount DECIMAL(18,2) NOT NULL,
    remaining_units DECIMAL(18,4) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Key Features
1. **Automatic Lot Creation**: Purchase transactions create lots
2. **FIFO Ordering**: Lots processed by purchase date
3. **Unit Tracking**: Remaining units updated after redemptions
4. **Gain/Loss Calculation**: Real-time capital gains computation
5. **Validation**: Ensures sufficient units for redemption

## 🎯 Use Cases

### 1. **Regular SIP Investments**
- Each SIP creates a new lot
- FIFO ensures oldest investments are redeemed first
- Helps in tax planning

### 2. **Lump Sum Investments**
- Large purchases create single lots
- Easy tracking of investment performance
- Clear cost basis for redemptions

### 3. **Partial Redemptions**
- Multiple lots may be partially consumed
- Accurate tracking of remaining units
- Detailed breakdown of gains/losses

### 4. **Tax Planning**
- Identify lots with specific holding periods
- Plan redemptions for tax efficiency
- Track short-term vs long-term gains

## 🔍 Testing the FIFO System

### Test Scenario 1: Simple Purchase and Redemption
```bash
# 1. Create purchase lot
curl -X POST "http://localhost:8080/api/fifo/create-purchase-lot" \
  -d "pan=ABCDE1234F&schemeId=1&folioNo=FOL001&purchaseDate=2024-01-01&purchaseNav=10.0000&purchaseUnits=100.0000&purchaseAmount=1000.00"

# 2. Calculate redemption
curl -X POST "http://localhost:8080/api/fifo/calculate-redemption" \
  -d "pan=ABCDE1234F&schemeId=1&folioNo=FOL001&redemptionUnits=50.0000&redemptionNav=15.0000&redemptionDate=2024-04-01"

# 3. Process redemption
curl -X POST "http://localhost:8080/api/fifo/process-redemption" \
  -d "pan=ABCDE1234F&schemeId=1&folioNo=FOL001&redemptionUnits=50.0000&redemptionNav=15.0000&redemptionDate=2024-04-01"
```

### Test Scenario 2: Multiple Lots
```bash
# Create multiple purchase lots
curl -X POST "http://localhost:8080/api/fifo/create-purchase-lot" \
  -d "pan=ABCDE1234F&schemeId=1&folioNo=FOL001&purchaseDate=2024-01-01&purchaseNav=10.0000&purchaseUnits=100.0000&purchaseAmount=1000.00"

curl -X POST "http://localhost:8080/api/fifo/create-purchase-lot" \
  -d "pan=ABCDE1234F&schemeId=1&folioNo=FOL001&purchaseDate=2024-02-01&purchaseNav=12.0000&purchaseUnits=50.0000&purchaseAmount=600.00"

# Redeem more than first lot
curl -X POST "http://localhost:8080/api/fifo/process-redemption" \
  -d "pan=ABCDE1234F&schemeId=1&folioNo=FOL001&redemptionUnits=120.0000&redemptionNav=15.0000&redemptionDate=2024-04-01"
```

## 📊 Expected Results

### After Multiple Lots Redemption:
- **First 100 units** from Jan 1 lot (₹10 NAV)
- **Next 20 units** from Feb 1 lot (₹12 NAV)
- **Total Cost Basis**: (100 × ₹10) + (20 × ₹12) = ₹1,240
- **Sale Proceeds**: 120 × ₹15 = ₹1,800
- **Capital Gain**: ₹1,800 - ₹1,240 = ₹560

## 🎉 Summary

The FIFO implementation provides:
- ✅ **Accurate capital gains calculation**
- ✅ **Automatic lot management**
- ✅ **Tax compliance support**
- ✅ **Detailed transaction tracking**
- ✅ **Real-time portfolio valuation**
- ✅ **Comprehensive API endpoints**

This makes your mutual fund transaction management system production-ready for financial institutions that need precise FIFO calculations for regulatory compliance and accurate financial reporting.
