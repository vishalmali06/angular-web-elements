import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit {

  
  @Input() currentPage!: number;
  @Input() totalPages!: number;

  @Output() onPage:  EventEmitter<Object> = new EventEmitter();

  range_size: number = 2;
  
  constructor() { }

  ngOnInit(): void {
  }

  onPageSelected(index: number){
    this.onPage.emit({index:index});
  }

  range(){
    if(this.totalPages==1)
      return [1]

    let real_total_pages = this.totalPages
    let real_page = this.currentPage+1
    let minimum = real_page - this.range_size
    let maximum = real_page + this.range_size

    if( minimum <= 1 ) {
        minimum = 1
        maximum = minimum + this.range_size*2
    }

    if( maximum >= real_total_pages ) {
        maximum = real_total_pages
        minimum = maximum - this.range_size*2
    }

    let start = minimum
    let stop = maximum+1

    if( this.totalPages <= (this.range_size*2 + 1) ) {
      start = 1
      stop = this.totalPages + 1
    }

    return new Array(stop-start)
      .fill(start)
      .map((number,index) => number + index )
  }

}
