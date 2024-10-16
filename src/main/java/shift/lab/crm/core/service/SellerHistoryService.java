package shift.lab.crm.core.service;

import shift.lab.crm.core.entity.Seller;
import shift.lab.crm.core.entity.enums.Operation;

public interface SellerHistoryService {
    void recordHistory(Seller seller, Operation operation);

}
