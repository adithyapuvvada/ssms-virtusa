export interface Users{
    id: number;
    username: string;
    role: {
        id: number;
        name: string;
    };
    companyId?: number;
}