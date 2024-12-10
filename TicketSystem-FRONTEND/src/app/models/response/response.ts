export class Response<T> {
    success : boolean;
    status : string;
    message : string;
    data : T;

    constructor(){
        this.success= false;
        this.status = "";
        this.message = "";
        this.data = {} as T;
    }
}
