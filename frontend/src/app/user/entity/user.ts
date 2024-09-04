import { Account } from "src/app/account/entity/account";


export class User {
    userId!: number;
    name!: string;
    emailId!: string;
    mobileNo!: string;
    secondaryMobileNo!: string;
    dob!: string;
    gender!: string;
    accounts!: Account[];
}