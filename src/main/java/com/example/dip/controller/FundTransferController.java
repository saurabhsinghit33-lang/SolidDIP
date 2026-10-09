package com.example.dip.controller;

import com.example.dip.domain.model.BankTransaction;
import com.example.dip.dto.TransferRequest;
import com.example.dip.dto.TransferResponse;
import com.example.dip.service.FundTransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
public class FundTransferController {

    private final FundTransferService transferService;

    public FundTransferController(FundTransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> executeTransfer(@RequestBody TransferRequest request) {
        BankTransaction txn = transferService.transferFunds(
                request.sourceAccount(),
                request.destinationAccount(),
                request.amount()
        );

        TransferResponse response = new TransferResponse(
                txn.transactionId(),
                txn.sourceAccount(),
                txn.destinationAccount(),
                txn.amount(),
                txn.status(),
                txn.timestamp()
        );

        return ResponseEntity.ok(response);
    }
}
