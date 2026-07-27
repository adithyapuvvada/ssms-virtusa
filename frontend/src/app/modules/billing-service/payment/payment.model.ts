export interface PaymentModel{
    id: number,
    paymentReference: string,
    invoiceId: number,
    amountPaid: number,
    paymentDate: string,
    paymentMode: string
}