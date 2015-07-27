define([], function() {
    return '' +
        '<div class="com.pentaho.det.data">' +
            '<h1>Main View - Plugin B</h1>'+
            '<!-- TODO: change to table directive -->' +
            '<div style="max-height: 200px; overflow: auto;">' +
                '<table class="table table-striped" data-st-table="viewModel.displayRowCollection" data-st-safe-src="viewModel.previewData.rows">'+
                    '<thead>'+
                        '<tr>'+
                        '<th data-ng-repeat="header in viewModel.previewData.cols" data-st-sort="viewModel.getRowValue( {{$index}} )">'+
                        '{{header.label}} ({{header.type}})'+
                        '</th>'+
                        '</tr>'+
                    '</thead>'+
                    '<tbody>'+
                        '<tr data-ng-repeat="row in viewModel.displayRowCollection">'+
                            '<td data-ng-repeat="cell in row.c"> {{cell.v}} </td>'+
                        '</tr>'+
                    '</tbody>'+
                '</table>'+
            '</div>' +

            '<a data-ui-sref=".DataB_Child">Show child of DataB</a>' +
            '<div data-ui-view></div>' +

        '</div>';
});