select op.id,
       op.author,
       op.category,
       op.comment,
       op.cost,
       op.creation_date,
       op.description,
       op.operation_date,
       op.place,
       op.operation_type,
       a.name as accountName,
       at.name as accounToTransferName,
       op.is_plan,
       op.last_updated,
       op.tags,
       op.parent_id,
       op.currency_cost
from operation op
         join account a on op.account_id = a.id
         left join account at on op.account_to_transfer_id = at.id;
