interface Invoice{
    invoiceId : number,
    invoiceNumber: string,
    shipmentId: number,
    customerName: string,
    amount: number,
    invoiceDate: Date,
    status: string,
    currencyCode?: string;
}