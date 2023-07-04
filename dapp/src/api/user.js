//用户提现
export function userWithdrawal(data) {
  return request.post(`userWithdrawal`, data);
}