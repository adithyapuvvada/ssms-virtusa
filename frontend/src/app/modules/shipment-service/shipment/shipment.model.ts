export interface Shipment{
    id: number,
    shipmentCode: string,
    companyId: number,
    description: string,
    volume: number,
    arrivalDate: string,
    dispatchDate: string,
    status: string,
    warehouse: {
        name: string,
        location: string,
        totalCapacity: number,
        usedCapacity: number,
        status: string
        }
}