export class PageParam {
    pageIndex = 1;
    pageSize = 5;
    constructor(pageIndex: number, pageSize: number) {
        this.pageIndex = pageIndex
        this.pageSize = pageSize
    }
}

export class OrderBy {
    field = '';
    way = 'desc';
    constructor(field: string, way: string) {
        this.field = field
        this.way = way
    }
}