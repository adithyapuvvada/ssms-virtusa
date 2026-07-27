interface inventory{
    id: number,
    itemName?:string,
    quantity?: number,
    unitPrice?:number,
    totalValue?:number,
    shipmentId?: number,
    shipmentCode?:string
}